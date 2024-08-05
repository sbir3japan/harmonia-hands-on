// SPDX-License-Identifier: Apache-2.0

/******************************************************************************
 * Copyright 2023 R3 LLC                                                      *
 *                                                                            *
 * Licensed under the Apache License, Version 2.0 (the "License");            *
 * you may not use this file except in compliance with the License.           *
 * You may obtain a copy of the License at                                    *
 *                                                                            *
 *     http://www.apache.org/licenses/LICENSE-2.0                             *
 *                                                                            *
 * Unless required by applicable law or agreed to in writing, software        *
 * distributed under the License is distributed on an "AS IS" BASIS,          *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   *
 * See the License for the specific language governing permissions and        *
 * limitations under the License.                                             *
 ******************************************************************************/

pragma solidity ^0.8.20;

import "openzeppelin/token/ERC20/ERC20.sol";

contract TestToken is ERC20 {
    uint256 constant _initial_supply = 3000;

    constructor(string memory name, string memory symbol) ERC20(name, symbol) {
        _mint(msg.sender, _initial_supply);
    }

    function decimals() public view override returns (uint8) {
        return 0;
    }
}
