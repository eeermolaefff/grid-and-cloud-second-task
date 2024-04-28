import React, { Component } from "react";
import Header from "./components/Header";
import Footer from "./components/Footer";
import Items from "./components/Items";
import Banner from "./components/Banner";
import axios from 'axios';
import Config from "./config.json";

const BACKEND_API = Config.BACKEND_API;

class App extends Component {

  constructor(props) {
    super(props);
    this.state = {
      account: null, 
      action: null, 
      bannerHeader: null,
      bannerMessage: null,
      items: null
    };

    this.handleOnItemClick = this.handleOnItemClick.bind(this);
    this.handleOnActionClick = this.handleOnActionClick.bind(this);
    this.handleOnCloseClick = this.handleOnCloseClick.bind(this);
    this.handleOnCreateButtonClick = this.handleOnCreateButtonClick.bind(this);
    this.initAccountsList = this.initAccountsList.bind(this);
    this.updateAccountsListScheduled = this.updateAccountsListScheduled.bind(this);

    require('node-schedule').scheduleJob("* * * * * *", async () => this.updateAccountsListScheduled());
  }


  render () {
    return (
      <div className={`${this.state.bannerHeader && 'root'}`}>
        <div className="wrapper">
            <Header account={this.state.account} onActionClick={this.handleOnActionClick} onCreateButtonClick={this.handleOnCreateButtonClick}/>
            <Items items={this.state.items} onCardClick={this.handleOnItemClick}/>
            <Footer />
        </div> 
        <div className={`${this.state.bannerHeader && 'bannered'}`}>{this.state.bannerHeader && (<Banner header={this.state.bannerHeader} message={this.state.bannerMessage} onCloseClick={this.handleOnCloseClick}></Banner>) }</div>
      </div>
    )
  }

  updateAccountsListScheduled() {
    if (this.state.bannerHeader)
      return;
    this.initAccountsList();
  }

  initAccountsList() {
    axios.get(BACKEND_API + "/accounts/get/all")
    .then(json => json.data)
    .then(json => {
      console.log(json)
      console.log(this.state)
      if (this.state.account) {
        this.setState({
          account: json.filter(ac => (ac.id === this.state.account.id))[0],
          items: json.filter(ac => (ac.id !== this.state.account.id))
        }, () =>  console.log(this.state));
      }
      return json;
    })
    .then(json => {
      if (!this.state.account) {
        this.setState({
          items: json
        }, () =>  console.log(this.state));
      }
      return json;
    })
  }

  handleOnActionClick(item) {
    this.setState({
      action: item
    });
  }  

  handleOnCreateButtonClick() {
    var accountName = document.getElementById('create-name').value;
    var accountSurname = document.getElementById('create-surname').value;
    var accountBalance = document.getElementById('create-balance').value;

    if (!accountName || accountName.length === 0 || !accountSurname || accountSurname.length === 0 || !accountBalance || accountBalance.length === 0) {
      this.setState({
        bannerHeader: "Error",
        bannerMessage: "Fill all fields before sending creating request"
      });   
      return;
    }

    axios.post(BACKEND_API + "/accounts/add", {
      name: accountName,
      surname: accountSurname,
      balance: accountBalance
    }).then(json => json.data)
    .then(json => {
      if (json.status === 'success') {
        this.initAccountsList()
      } else {
        this.setState({
          bannerHeader: "Error",
          bannerMessage: json.reason
        });   
      }
    })
  }
  
  handleOnCloseClick() {
    this.setState({
      bannerHeader: null,
      bannerMessage: null
    });
  }

  handleOnItemClick(item) {
    if (this.state.action === 'login') {

      this.setState({
        items: this.state.account ? this.state.items.push(this.state.account) : this.state.items,
        account: item
      });
      this.setState({
        items: this.state.items.filter(it => (it.id !== item.id))
      });

    } else if (this.state.action === 'delete') {

      axios.post(BACKEND_API + "/accounts/delete", item)
      .then(json => json.data)
      .then(json => {
        console.log(json)
        if (json.status === 'success') {
          this.initAccountsList()
        } else {
          this.setState({
            bannerHeader: "Error",
            bannerMessage: json.reason
          });   
        }
      })

    } else if (this.state.action === 'transfer') {

      if (this.state.account) {
        var transferAmount = document.getElementById('transfer-amount').value

        console.log(this.state.account)
        console.log(item)
        console.log(transferAmount)
  

        if (transferAmount <= 0) {
          this.setState({
            bannerHeader: "Error",
            bannerMessage: "Amount of money you want to transfer should be positive"
          });   
        } else {
          axios.post(BACKEND_API + "/transfer", {
            accountIdFrom: this.state.account.id,
            accountIdTo: item.id,
            amount: transferAmount
          })
          .then(json => json.data)
          .then(json => {
            console.log(json)
            if (json.status === 'success') {
              this.initAccountsList()
            } else {
              this.setState({
                bannerHeader: "Error",
                bannerMessage: json.reason
              });   
            }
          })
        }
      }
    }
  }
}


export default App;
