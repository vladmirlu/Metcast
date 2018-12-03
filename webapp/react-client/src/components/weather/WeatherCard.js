import React, {Component} from 'react';
import './Card.css';
import {Button} from "antd";

class WeatherCard extends Component {
    constructor(props) {
        super(props);
        this.state = {
            contentVisible: false
        }
    }

    setContentVisibility(){
        this.setState({
            contentVisible: this.state.contentVisible === false
        });
    };

    render() {
        return (
            <div className="card-content" >
                <div className="card-location col-md-8">
                    {this.props.card.location}
                </div>
                <Button className="ant-btn-primary col-md-2" onClick={this.setContentVisibility.bind(this)}>
                    {
                        this.state.contentVisible ? 'Hide' : 'Show'
                    }
                </Button>
                <Button className="ant-btn-danger col-md-2" value={this.props.card.location} onClick={this.props.handleCardDelete}>
                    Delete
                </Button>
                <div>
                    {
                        this.state.contentVisible ? (
                            <table className="table table-bordered">
                                <thead>
                                <tr>
                                    <th className='desc-col'>Date and time</th>
                                    <th className='desc-col'>Weather icon</th>
                                    <th className='desc-col'>Weather description</th>
                                    <th className='desc-col'>Temperature C</th>
                                    <th className='desc-col'>Precipitation mm</th>
                                    <th className='desc-col'>Pressure mlbars</th>
                                    <th className='desc-col'>Humidity %</th>
                                    <th className='desc-col'>Visibility km</th>
                                    <th className='desc-col'>Cloud cover %</th>
                                    <th className='desc-col'>Wind speed km/h</th>
                                </tr>
                                </thead>
                                <tbody>
                                {
                                    this.props.card.weatherUnits.map((unit) => {
                                        return <tr>
                                            <td className='desc-col'>{unit.dateTime}</td>
                                            <td className='desc-col'>
                                                <img src={unit.weatherIconUrl}>
                                                </img>
                                            </td>
                                            <td className='desc-col'>{unit.weatherDescription}</td>
                                            <td className='desc-col'>{unit.tempCelsius}</td>
                                            <td className='desc-col'>{unit.precipitationMM}</td>
                                            <td className='desc-col'>{unit.pressureMillibars}</td>
                                            <td className='desc-col'>{unit.humidityPercent}</td>
                                            <td className='desc-col'>{unit.visibilityKm}</td>
                                            <td className='desc-col'>{unit.cloudCoverPercent}</td>
                                            <td className='desc-col'>{unit.windSpeedKmPerHour}</td>
                                        </tr>
                                    })
                                }
                                </tbody>
                            </table>
                        ) : null
                    }
                </div>
            </div>
        );
    }
}


export default WeatherCard;