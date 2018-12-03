import React, {Component} from "react";
import {Button, Form, Input} from "antd";

const FormItem = Form.Item;

export default class LocationInput extends Component {

    constructor(props) {
        super(props);

        this.state = {
            location: ''
        }
    }

    handleLocationChange = event => {
        console.log(event.target.value);
        this.setState({
            location: event.target.value
        });
        this.props.addLocation(event);
    };

    render() {

        return (
            <FormItem className="card-form-row ">
                      <span className='col-md-10'>
                          <Input placeholder="Enter the location"
                                 autosize={{maxRows: 3}}
                                 name="location"
                                 value={this.state.location}
                                 onChange={this.handleLocationChange}/>
                      </span>
                <span className='col-md-2'> <Button className="ant-btn-danger" value={this.state.location}
                               onClick={this.props.removeField}> Delete location</Button> </span>
            </FormItem>
        )
    }
}