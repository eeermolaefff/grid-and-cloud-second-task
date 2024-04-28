import React, { Component } from 'react'
import Item from './Item'

export class Items extends Component {
  render() {
    return (
      <main>
        <div className='items-header'>
            <h1>Users:</h1>
        </div>

        <div className='items-list'>
        {
          
          (this.props.items) && this.props.items.slice().sort((a, b) => {
            if (a.id < b.id)        return -1;
            else if (a.id > b.id)   return 1;
            return 0;
          }).map(el => (
            <Item key={el.id} item={el} onCardClick={this.props.onCardClick}></Item>
          ))
        }
        </div>
      </main>
    )
  }
}

export default Items