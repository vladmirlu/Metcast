import React, {Component} from 'react';
import {addWeatherCardList} from '../util/APIUtils';
import {Form, Button, notification} from 'antd';
import LocationInput from "./LocationInput";
import {Icon} from 'antd';
const FormItem = Form.Item;


class AddWeatherCard extends Component {
    constructor(props) {
        super(props);

        this.state = {
            locations: [1],
            username: this.props.user.username

        };
        this.handleAddWeatherCard = this.handleAddWeatherCard.bind(this);
    }


    handleAddWeatherCard(event) {
        event.preventDefault();

        addWeatherCardList(this.state)
            .then(response => {
                notification.success({
                    message: 'Metcast App',
                    description: 'The new weather card for ' + this.state.locations + ' created successfully'
                });

                this.props.setCardIds(response);
            }).catch(error => {
            if (error.status === 401) {
                this.props.handleLogout('/login', 'error', 'You have been logged out. Please login create poll.');
            } else {
                notification.error({
                    message: 'Metcast App',
                    description: error.message || 'Sorry! Something went wrong. Please try again!'
                });
            }
        });
    };

    removeField=(event, index)=> {
        this.state.locations.splice(index, 1) ;
        this.setState({
            locations: this.state.locations
        });
    };

    addMoreLocations = () =>{
        this.state.locations.push(1);
        this.setState({
            locations: this.state.locations
        });
    };

    addLocation (event, index) {
        console.log(event.target.value);
        this.state.locations[index] = event.target.value;
        this.setState({
            locations: this.state.locations
        });
    };

    render() {

        const locationViews = [];
        this.state.locations.forEach((field, index) => {
            locationViews.push(<LocationInput field={field}
                                              removeField={(event) => this.removeField(event, index)}
                                              addLocation={(event) => this.addLocation(event, index)}
            />)
        });

        return (
            <div className="new-card-container">
                <div className='col-md-10'> <h1>Create weather cards</h1> </div>
                <div className="col-md-2">
                    <Button type="dashed" onClick={this.addMoreLocations} disabled={this.state.locations.length > 10}>
                        <Icon type="plus" /> Add location
                    </Button>
                </div>

                    <Form onSubmit={this.handleAddWeatherCard}>
                        {locationViews}
                        <FormItem>
                            <Button type="primary"
                                    htmlType="submit"
                                    size="large"
                                    className="create-card-form-button">Submit data to create</Button>
                        </FormItem>
                    </Form>
            </div>
        );
    }
}

export default AddWeatherCard;