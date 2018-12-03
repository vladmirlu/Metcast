import React, {Component} from 'react';
import {deleteWeatherCard, getUserCreatedWeatherCards} from '../util/APIUtils';
import WeatherCard from './WeatherCard';
import {Button, Icon, notification} from 'antd';
import {Switch, withRouter} from 'react-router-dom';
import './WeatherCardList.css';


class WeatherCardList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            weatherCards: []
        };
            this.loadWeatherCards = this.loadWeatherCards.bind(this);
            this.loadWeatherCards();
    }

   /* componentDidMount() {
        this.loadWeatherCards();
    }*/
    componentDidUpdate(nextProps) {
        if (this.props.cardIds !== nextProps.cardIds) {
            this.loadWeatherCards();
        }
    }

    loadWeatherCards() {

        if (this.props.user) {
            getUserCreatedWeatherCards(this.props.user.username)
                .then(response => {
                    this.setState({
                        weatherCards: response
                    });
                }).catch(error => {
                notification.error({
                    message: 'Metcast App',
                    description: 'Error! Could not fetch weather cards'
                });
            });
        } else {
            notification.error({
                message: 'Metcast App',
                description: 'Error! Could not define current user'
            });
        }
    }

    handleCardDelete(event, cardIndex) {

        deleteWeatherCard(event.target.value)
            .then(response => {
                notification.success({
                    message: 'Metcast App',
                    description: 'The new weather card for location ' + response.location + ' deleted successfully'
                });
                this.state.weatherCards.splice(cardIndex, 1);
                this.setState({
                    weatherCards: this.state.weatherCards
                });
            }).catch(error => {
            if (error.status === 401) {
                this.props.handleLogout('/login', 'error', 'You have been logged out. Please login to delete current card.');
            } else {
                notification.error({
                    message: 'Metcast App',
                    description: error.message || 'Sorry! Something went wrong. Please try again!'
                });
            }
        });
    }


    render() {
        const weatherCardViews = [];
        this.state.weatherCards.forEach((card, cardIndex) => {
            weatherCardViews.push(<WeatherCard key={card.id} card={card}
                                               handleCardDelete={(event) => this.handleCardDelete(event, cardIndex)}
            />)
        });

        return (

            <div className="cards-container">
                {weatherCardViews}
                {
                    this.state.weatherCards.length === 0 ? (
                        <div className="no-cards-found">
                            <span>No Weather Card Found.</span>
                        </div>
                    ) : null
                }
            </div>

        );
    }
}

export default withRouter(WeatherCardList);