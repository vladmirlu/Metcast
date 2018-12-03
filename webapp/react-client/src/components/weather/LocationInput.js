import React, {Component} from "react";
import {Button, Form, Input} from "antd";

const FormItem = Form.Item;

export default class LocationInput extends Component {

    constructor(props){
    super(props);

        this.state = {
            location: ''
        }
}

    handleLocationChange = event => {
        this.setState({
            location: event.target.value
        });
    };

    render() {

        return (
            <FormItem className="card-form-row col-md-10">
                        <Input
                            placeholder="Enter the location"
                            style={{fontSize: '16px'}}
                            autosize={{maxRows: 3}}
                            name="location"
                            value={this.state.location}
                            onChange={this.handleLocationChange}/>
                <Button className="ant-btn-danger col-md-2" value={this.state.location} onClick={this.props.handleCardDelete}>
                    Delete
                </Button>
            </FormItem>
        )
    }
}