import React, { Component } from 'react'

export class Account extends Component {
  render() {
    return (
        <div className='item'>

            <h1>Your account:</h1>
            <img 
                alt='Profile'
                src={"./img/" + this.props.account.name + '-' + this.props.account.surname + '.jpg'} 
                onError={ event => {
                    if (event.target.src !== './img/default.jpg') 
                    event.target.src = './img/default.jpg'
                    event.onerror = null
                }}>
            </img>
            <h2>{this.props.account.name}</h2>
            <h2>{this.props.account.surname}</h2>
            <div className='item-info'>
                <h3>Balance:</h3>
                <p>{parseFloat(this.props.account.balance.toFixed(3))}$</p>
            </div>
        </div>
    )
  }
}

export default Account