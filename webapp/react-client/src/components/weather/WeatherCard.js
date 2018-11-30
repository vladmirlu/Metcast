import React, { Component } from 'react';
import './Card.css';

class WeatherCard extends Component {

    render() {
        return (
            <div className="card-content">
                    <div className="card-question">
                        {this.props.card.location}
                    </div>
            </div>
        );
    }
}

export default WeatherCard;