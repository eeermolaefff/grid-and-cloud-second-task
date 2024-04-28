import React, { Component } from 'react'

export class TransferCart extends Component {
  render() {
    if (this.props.account)
        return (
            <div>
                <h4 className='firstTransferHeader'>Transfer mode is active</h4>
                <p>Enter the sum you want to transfer and click on any user card below to chose the recipient</p>
                <h4>Amount:</h4>
                <input type="number" id="transfer-amount"/> 
            </div>
        )
    else 
        return (
            <div>
                <h4>Transfer mode can not be activated</h4>
                <p>Log in to your account before making money transfers</p>
            </div>
    )
  }
}

export default TransferCart