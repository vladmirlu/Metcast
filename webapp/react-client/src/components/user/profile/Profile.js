import React, { Component } from 'react';
import {createWeatherCard, getUserProfile} from '../../util/APIUtils';
import {Avatar, Button, Form, Input, notification, Tabs} from 'antd';
import { getAvatarColor } from '../../util/Colors';
import LoadingIndicator  from '../../common/LoadingIndicator';
import './Profile.css';
import NotFound from '../../common/NotFound';
import ServerError from '../../common/ServerError';
import WeatherCardList from "../../weather/WeatherCardList";

const FormItem = Form.Item;
const { TextArea } = Input;

const TabPane = Tabs.TabPane;

class Profile extends Component {
    constructor(props) {
        super(props);
        this.state = {
            user: null,
            isLoading: false
        };

        this.loadUserProfile = this.loadUserProfile.bind(this);
    }

    loadUserProfile(username) {
        this.setState({
            isLoading: true
        });

        getUserProfile(username)
        .then(response => {
            this.setState({
                user: response,
                isLoading: false
            });
        }).catch(error => {
            if(error.status === 404) {
                this.setState({
                    notFound: true,
                    isLoading: false
                });
            } else {
                this.setState({
                    serverError: true,
                    isLoading: false
                });        
            }
        });        
    }
      
    componentDidMount() {
        const username = this.props.match.params.username;
        this.loadUserProfile(username);
    }

    componentDidUpdate(nextProps) {
        if(this.props.match.params.username !== nextProps.match.params.username) {
            this.loadUserProfile(nextProps.match.params.username);
        }        
    }

    handleSubmit(event) {
        event.preventDefault();
        const cardData = {
            location: this.state.location,
            userDTO: {
               email: this.state.user.email
            }
        };

        createWeatherCard(cardData)
            .then(response => {
                this.props.history.push("/");
            }).catch(error => {
            if(error.status === 401) {
                this.props.handleLogout('/login', 'error', 'You have been logged out. Please login create poll.');
            } else {
                notification.error({
                    message: 'Polling App',
                    description: error.message || 'Sorry! Something went wrong. Please try again!'
                });
            }
        });
    };

    handleLocationChange(event) {
        const value = event.target.value;
        this.setState({
            location: value
        });
    }

    render() {
        if(this.state.isLoading) {
            return <LoadingIndicator />;
        }

        if(this.state.notFound) {
            return <NotFound />;
        }

        if(this.state.serverError) {
            return <ServerError />;
        }

        const tabBarStyle = {
            textAlign: 'center'
        };

        return (
            <div className="profile">
                {
                    this.state.user ? (
                        <div className="user-profile">
                            <div className="user-details">
                                <div className="user-avatar">
                                    <Avatar className="user-avatar-circle" style={{ backgroundColor: getAvatarColor(this.state.user.username)}}>
                                        {this.state.user.username[0].toUpperCase()}
                                    </Avatar>
                                </div>
                                <div className="user-summary">
                                    <div className="full-name">{this.state.user.username}</div>
                                    <div className="username">@{this.state.user.username}</div>
                                </div>
                            </div>
                            <div className="user-poll-details">
                                <Tabs defaultActiveKey="1" 
                                    animated={false}
                                    tabBarStyle={tabBarStyle}
                                    size="large"
                                    className="profile-tabs">
                                    <TabPane tab={`${this.state.user.username} Weather Cards`} key="1">
                                        <WeatherCardList username={this.props.match.params.username} />
                                    </TabPane>
                                </Tabs>
                            </div>

                            <div className="new-poll-content">
                                <Form onSubmit={this.handleSubmit} className="create-poll-form">
                                    <FormItem className="card-form-row">
                        <TextArea
                            placeholder="Enter the location"
                            style = {{ fontSize: '16px' }}
                            autosize={{ minRows: 3, maxRows: 6 }}
                            name = "location"
                            value = {this.state.location}
                            onChange = {this.handleLocationChange} />
                                    </FormItem>

                                    <FormItem className="card-form-row">
                                        <Button type="primary"
                                                htmlType="submit"
                                                size="large"
                                                className="create-poll-form-button">Create Poll</Button>
                                    </FormItem>
                                </Form>
                            </div>

                        </div>  
                    ): null               
                }
            </div>
        );
    }
}

export default Profile;