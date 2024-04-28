import React, { useState } from 'react'
import Account from './Account'
import { LoginCart } from './LoginCart'
import { TransferCart } from './TransferCart'
import CreateCart from './CreateCart'
import { DeleteCart } from './DeleteCart'

export default function Header(props) {
  let [createOpened, setCreateOpened] = useState(false)
  let [loginOpened, setLoginOpened] = useState(false)
  let [transferOpened, setTransferOpened] = useState(false)
  let [deleteOpened, setDeleteOpened] = useState(false)

  return (
    <header>
        <div>
            <span className='logo'>Bank Simulator</span>
            <ul className='nav'>
                <li onClick={() => { setDeleteOpened(deleteOpened = !deleteOpened); setLoginOpened(false); setTransferOpened(false); setCreateOpened(false); props.onActionClick(deleteOpened ? 'delete' : null)}} className={`${deleteOpened && 'active'}`}>Delete account</li>
                <li onClick={() => { setCreateOpened(createOpened = !createOpened); setLoginOpened(false); setTransferOpened(false); setDeleteOpened(false); props.onActionClick(createOpened ? 'create' : null)}} className={`${createOpened && 'active'}`}>Create account</li>
                <li onClick={() => { setLoginOpened(loginOpened = !loginOpened); setCreateOpened(false); setTransferOpened(false); setDeleteOpened(false); props.onActionClick(loginOpened ? 'login' : null)}} className={`${loginOpened && 'active'}`}>Log in</li>
                <li onClick={() => { setTransferOpened(transferOpened = !transferOpened); setLoginOpened(false); setCreateOpened(false); setDeleteOpened(false); props.onActionClick(transferOpened ? 'transfer' : null)}} className={`${transferOpened && 'active'}`}>Transfer money</li>
            </ul>

            {deleteOpened && (<div className='delete-cart'><DeleteCart key={-1}></DeleteCart></div>)}
            {createOpened && (<div className='create-cart'><CreateCart key={-3} onCreateButtonClick={props.onCreateButtonClick}></CreateCart></div>)}
            {loginOpened && (<div className='login-cart'><LoginCart key={-1}></LoginCart></div>)}
            {transferOpened && (<div className='transfer-cart'><TransferCart key={-2} account={props.account}></TransferCart></div>)}
            {props.account && (<div className='account-cart'><Account key={props.account.id} account={props.account}></Account></div>)}
        </div>
        <div className='presentation'></div>
    </header>

  )
}
