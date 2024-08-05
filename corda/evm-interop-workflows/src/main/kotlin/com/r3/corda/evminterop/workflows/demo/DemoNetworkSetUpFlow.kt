package com.r3.corda.evminterop.workflows.demo

import co.paralleluniverse.fibers.Suspendable
import com.r3.corda.evminterop.workflows.UnsecureRemoteEvmIdentityFlow
import net.corda.core.flows.FlowLogic
import net.corda.core.flows.InitiatingFlow
import net.corda.core.flows.StartableByRPC

@StartableByRPC
@InitiatingFlow
class DemoNetworkSetUpFlow(
    private val privateKey: String,
    private val protocolAddress: String,
    private val evmDeployerAddress: String
) : FlowLogic<Unit>() {

    @Suspendable
    override fun call() {
        val jsonRpcEndpoint = "http://localhost:8545"
        val chainId: Long = 1337
        subFlow(
            UnsecureRemoteEvmIdentityFlow(
                privateKey,
                jsonRpcEndpoint,
                chainId,
                protocolAddress,
                evmDeployerAddress
            )
        )
    }
}