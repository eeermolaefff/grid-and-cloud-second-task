import React, { Component } from 'react'

export class Banner extends Component {
  render() {
    return (
        <banner>
          <div className='banner-info'>
            <h2>{this.props.header}</h2>
            <m>{this.props.message}</m>
          </div>
          <div className='close-button' onClick={() => this.props.onCloseClick()}>close</div>
        </banner>
    )
  }
}

export default Banner