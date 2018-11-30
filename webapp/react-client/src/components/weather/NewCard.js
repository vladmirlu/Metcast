import React, { Component } from 'react';
import { createWeatherCard } from '../util/APIUtils';
import { Form, Input, Button, notification } from 'antd';
const FormItem = Form.Item;
const { TextArea } = Input;

class NewCard extends Component {
    constructor(props) {
        super(props);
        this.state = {
            location: '',

            userDTO: {
                email: this.state.user.email
            }
        };

        console.log(this.state.userDTO);
        this.handleSubmit = this.handleSubmit.bind(this);
    }


    handleSubmit(event) {
        event.preventDefault();
        const cardData = {
            location: this.state.location,
            userDTO: this.state.userDTO
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

        return (
            <div className="new-card-container">
                <h1 className="page-title">Create Poll</h1>
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
        );
    }
}

export default NewCard;