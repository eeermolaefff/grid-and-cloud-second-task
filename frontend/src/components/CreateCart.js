import React, { Component } from 'react'

export class CreateCart extends Component {
  render() {
    return (
        <div>
            <h4 className='firstCreateHeader'>Creation mode is active</h4>
            <p>Enter the properties of account you want to create</p>
            <h4>Name:</h4>
            <input type="text" id="create-name"/> 
            <h4>Surame:</h4>
            <input type="text" id="create-surname"/> 
            <h4>Balance:</h4>
            <input type="number" id="create-balance"/> 
            <div className='create-button' onClick={() => this.props.onCreateButtonClick()}>create</div>
        </div>
    )
  }
}

export default CreateCart