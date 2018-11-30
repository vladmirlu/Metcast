import React, {Component} from 'react';
import {getUserCreatedWeatherCards} from '../util/APIUtils';
import WeatherCard from './WeatherCard';
import LoadingIndicator from '../common/LoadingIndicator';
import {Button, Icon, notification} from 'antd';
import {Switch, withRouter} from 'react-router-dom';
import './WeatherCardList.css';


class WeatherCardList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            weatherCards: [],
            isLoading: false
        };
        this.loadWeatherCards = this.loadWeatherCards.bind(this);
    }

    loadWeatherCards() {
        console.log(this.props.username);
        let promise;
        if (this.props.username) {
            promise = getUserCreatedWeatherCards(this.props.username);
        } else {
            alert(this.props.username)
        }
        if (!promise) {
            return;
        }
        this.setState({
            isLoading: true
        });

        promise
            .then(response => {
                const cards = this.state.weatherCards.slice();

                this.setState({
                    weatherCards: cards.concat(response.content),
                    isLoading: false
                })
            }).catch(error => {
            this.setState({
                isLoading: false
            })
        });

    }

    render() {
        const weatherCardViews = [];
        this.state.weatherCards.forEach((card) => {
            weatherCardViews.push(<WeatherCard
                key={card.id}
                card={card}
                />)
        });

        return (
            <div className="cards-container">
                {weatherCardViews}
                {
                    !this.state.isLoading && this.state.weatherCards.length === 0 ? (
                        <div className="no-cards-found">
                            <span>No Polls Found.</span>
                        </div>
                    ) : null
                }
                {
                    !this.state.isLoading ? (
                        <div className="load-more-cards">
                           {/* <Button type="dashed" onClick={this.loadWeatherCards()} disabled={this.state.isLoading}>
                                <Icon type="plus"/> Load more
                            </Button>*/}

                                {/*<PrivateRoute authenticated={this.state.isAuthenticated} path="/weather/card/add" component={NewCard} handleLogout={this.handleLogout} />*/}
                        </div>

                    ) : null
                }
                {
                    this.state.isLoading ?
                        <LoadingIndicator/> : null
                }
            </div>
        );
    }
}

export default withRouter(WeatherCardList);