import React, { Component } from 'react'

export class Item extends Component {
  render() {
    return (
        <div className='item' onClick={() => this.props.onCardClick(this.props.item)}>
            <img 
              alt='Profile'
              src={"./img/" + this.props.item.name + '-' + this.props.item.surname + '.jpg'} 
              onError={ event => {
                if (event.target.src !== './img/default.jpg') 
                  event.target.src = './img/default.jpg'
                event.onerror = null
              }}>
            </img>
            <h2>{this.props.item.name} {this.props.item.surname}</h2>
            <div className='item-info'>
                <h3>Balance:</h3>
                <p>{parseFloat(this.props.item.balance.toFixed(3))}$</p>
            </div>
        </div>
    )
  }
}

export default Item