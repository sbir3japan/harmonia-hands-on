require("@nomiclabs/hardhat-waffle");
require("@nomicfoundation/hardhat-foundry");

task("accounts", "Prints the list of accounts", async (taskArgs, hre) => {
  const accounts = await hre.ethers.getSigners();

  for (const account of accounts) {
    console.log(account.address);
  }
});

/**
 * @type import('hardhat/config').HardhatUserConfig
 */
module.exports = {
  solidity: "0.8.20",
  optimizer: { enabled: true },
  networks: {
    hardhat: {
      // mining: {
      //   mempool: {
      //     order: "fifo",
      //   },
      //   auto: false,
      //   interval: 1000,
      // },
      chainId: 1337,
    },
    besu: {
      url: "http://localhost:8545",
      accounts: [
        "0x8bbbb1b345af56b560a5b20bd4b0ed1cd8cc9958a16262bc75118453cb546df7",
        "0x4762e04d10832808a0aebdaa79c12de54afbe006bfffd228b3abcc494fe986f9",
        "0x61dced5af778942996880120b303fc11ee28cc8e5036d2fdff619b5675ded3f0",
        "0xe6181caaffff94a09d7e332fc8da9884d99902c7874eb74354bdcadf411929f1",
        "0x5ad8b28507578c429dfa9f178d7f742f4861716ee956eb75648a7dbc5ffe915d",
        "0xf23f92ed543046498d7616807b18a8f304855cb644df25bc7d0b0b37d8a66019",
        "0x7f012b2a11fc651c9a73ac13f0a298d89186c23c2c9a0e83206ad6e274ba3fc7",
      ],
    },
  },
  paths: {
    sources: "./src",
    tests: "./test",
    cache: "./build/cache",
    artifacts: "./build/out",
  },
  foundry: {
    remappings: "./remappings.txt",
  },
};
