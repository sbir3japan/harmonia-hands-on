package com.r3.corda.evminterop.workflows.demo

import co.paralleluniverse.fibers.Suspendable
import com.r3.corda.evminterop.dto.TransactionReceipt
import com.r3.corda.evminterop.services.evmInterop
import com.r3.corda.evminterop.services.swap.DraftTxService
import com.r3.corda.evminterop.workflows.swap.ClaimCommitmentWithSignatures
import net.corda.core.crypto.SecureHash
import net.corda.core.flows.FlowLogic
import net.corda.core.flows.InitiatingFlow
import net.corda.core.flows.StartableByRPC

@StartableByRPC
@InitiatingFlow
class DemoClaimCommitment(
    private val transactionId: SecureHash
) : FlowLogic<TransactionReceipt>() {

    @Suspendable
    override fun call(): TransactionReceipt {

        // collect the EVM verifiable signatures that attest that the draft transaction was signed by the notary
        val signatures =
            serviceHub.cordaService(DraftTxService::class.java).notarizationProofs(transactionId)

        // Bob can claim Alice's EVM committed asset
        return subFlow(ClaimCommitmentWithSignatures(transactionId, signatures))
    }
}