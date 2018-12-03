import React, { Component } from 'react';
import './App.css';
import {
  Route,
  withRouter,
  Switch
} from 'react-router-dom';

import { getCurrentUser } from './util/APIUtils';
import { ACCESS_TOKEN } from './constants';

import WeatherCardList from './weather/WeatherCardList';
import Login from './user/login/Login';
import Signup from './user/signup/Signup';
import Profile from './user/profile/Profile';
import AppHeader from './common/AppHeader';
import NotFound from './common/NotFound';
import LoadingIndicator from './common/LoadingIndicator';
import { Layout, notification } from 'antd';
const { Content } = Layout;

class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      currentUser: null,
      isAuthenticated: false,
      isLoading: false
    }
    this.handleLogout = this.handleLogout.bind(this);
    this.loadCurrentUser = this.loadCurrentUser.bind(this);
    this.handleLogin = this.handleLogin.bind(this);

    notification.config({
      placement: 'topRight',
      top: 70,
      duration: 3,
    });    
  }

  loadCurrentUser() {
    this.setState({
      isLoading: true
    });
    getCurrentUser()
    .then(response => {
      this.setState({
        currentUser: response,
        isAuthenticated: true,
        isLoading: false
      });
    }).catch(error => {
      this.setState({
        isLoading: false
      });  
    });
  }

  componentDidMount() {
    this.loadCurrentUser();
  }

  handleLogout(redirectTo="/", notificationType="success", description="You're successfully logged out.") {
    localStorage.removeItem(ACCESS_TOKEN);

    this.setState({
      currentUser: null,
      isAuthenticated: false
    });

    this.props.history.push(redirectTo);
    
    notification[notificationType]({
      message: 'Polling App',
      description: description,
    });
  }

  handleLogin() {
    notification.success({
      message: 'Polling App',
      description: "You're successfully logged in.",
    });
    this.loadCurrentUser();
    this.props.history.push("/");
  }

  render() {
    if(this.state.isLoading) {
      return <LoadingIndicator />
    }
    return (
        <Layout className="app-container">
          <AppHeader isAuthenticated={this.state.isAuthenticated} 
            currentUser={this.state.currentUser} 
            onLogout={this.handleLogout} />

          <Content className="app-content">
            <div className="container">
              <Switch>      
                <Route exact path="/" 
                  render={(props) => <WeatherCardList isAuthenticated={this.state.isAuthenticated}
                      currentUser={this.state.currentUser} handleLogout={this.handleLogout} {...props} />}>
                </Route>
                <Route path="/login" render={(props) => <Login onLogin={this.handleLogin} {...props} />} />
                <Route path="/signup" component={Signup} />
                <Route path="/users/:username"
                  render={(props) => <Profile isAuthenticated={this.state.isAuthenticated} currentUser={this.state.currentUser} {...props}  />}>
                </Route>
                <Route component={NotFound} />
              </Switch>
            </div>
          </Content>
        </Layout>
    );
  }
}

export default withRouter(App);


/*
export default class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = { data: []};
        this.fetchData();
        console.log(this.state)
    }

    fetchData() {
        // Where we're fetching data from
        fetch('http://localhost:8181/api/weather/card/get-all/qwer')
        // We get the API response and receive data in JSON format...
            .then(response => response.json())
            // ...then we update the users state
            .then(data =>
                this.setState({
                    data: data,
                })
            )
            // Catch any errors we hit and update the app
            .catch(error => this.setState({ error, isLoading: false }));
    }


    render() {
        return (
            <div>
                {/!* { <Add selectedMonth={this.state.selectedMonth} selectedYear={this.state.selectedYear} />}*!/}
                <table>
                    <thead>
                    <tr><th></th>
                        <th className='desc-col'>Weather Card</th>

                        <th className='button-col'>Delete</th></tr>
                    </thead>
                    <tbody>
                    {
                        this.state.data.map((exp) => {
                            return  <tr><td className='counterCell'></td>
                                <td className='desc-col'>{exp.location}
                                    <li>
                                        <ul className='desc-col'>{exp.weatherUnits}</ul>
                                    </li>
                                </td>

                                <td className='button-col'><Delete expense={exp} /></td>
                            </tr>
                        })
                    }
                    </tbody>
                </table>
            </div>
        );
    }
}*/
