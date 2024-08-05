package com.r3.corda.evminterop.workflows.demo

import co.paralleluniverse.fibers.Suspendable
import com.r3.corda.evminterop.SwapVaultEventEncoder
import com.r3.corda.evminterop.states.swap.SwapTransactionDetails
import com.r3.corda.evminterop.workflows.swap.BuildAndProposeDraftTransactionFlow
import net.corda.core.contracts.OwnableState
import net.corda.core.contracts.StateRef
import net.corda.core.crypto.SecureHash
import net.corda.core.flows.FlowLogic
import net.corda.core.flows.InitiatingFlow
import net.corda.core.flows.StartableByRPC
import java.math.BigInteger

@StartableByRPC
@InitiatingFlow
class DemoBuildAndProposeDraftTransactionFlow(
    private val transactionId: SecureHash,
    private val outputIndex: Int,
    private val buyerAddress: String,
    private val buyerCordaName: String,
    private val sellerAddress: String,
    private val signerAddress: String,
    private val signerCordaName: String,
    private val tokenAddress: String,
    private val protocolAddress: String,
    private val amount: Int
) : FlowLogic<SecureHash>() {

    @Suspendable
    override fun call(): SecureHash {
        val swapTxDetails = buildSwapTransactionDetails(
            buyerAddress,
            buyerCordaName,
            sellerAddress,
            signerAddress,
            signerCordaName,
            tokenAddress,
            protocolAddress,
            amount
        )
        val notary = serviceHub.networkMapCache.notaryIdentities[0]
        return subFlow(BuildAndProposeDraftTransactionFlow(swapTxDetails, notary))!!.id
    }

    @Suspendable
    private fun buildSwapTransactionDetails(
        buyerAddress: String,
        buyerCordaName: String,
        sellerAddress: String,
        signerAddress: String,
        signerCordaName: String,
        tokenAddress: String,
        protocolAddress: String,
        amount: Int
    ): SwapTransactionDetails {
        // Construct the StateRef of the asset you want to spend
        val inputStateRef = StateRef(transactionId, outputIndex)

        // Retrieve the input state (asset X) from the vault using the StateRef
        val inputStateAndRef = serviceHub.toStateAndRef<OwnableState>(inputStateRef)

        val swapVaultEventEncoder = SwapVaultEventEncoder.create(
            chainId = BigInteger.valueOf(1337),
            protocolAddress = protocolAddress,
            owner = buyerAddress,
            recipient = sellerAddress,
            amount = amount.toBigInteger(),
            tokenId = BigInteger.ZERO,
            tokenAddress = tokenAddress,
            signaturesThreshold = BigInteger.ONE,
            signers = listOf(signerAddress) // same as validators but the EVM identity instead
        )

        return SwapTransactionDetails(
            senderCordaName = ourIdentity,
            receiverCordaName = serviceHub.identityService.partiesFromName(
                buyerCordaName,
                false
            ).single(),
            cordaAssetState = inputStateAndRef,
            approvedCordaValidators = serviceHub.identityService.partiesFromName(
                signerCordaName,
                false
            ).toList(),
            minimumNumberOfEventValidations = 1,
            unlockEvent = swapVaultEventEncoder
        )
    }
}