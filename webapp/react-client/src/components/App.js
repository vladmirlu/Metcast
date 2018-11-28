import React from 'react';
import ReactDOM from 'react-dom';
import axios from 'axios';
import Add from './Add';
import Delete from './Delete';
import { Tab, Tabs } from 'react-bootstrap';
import WeatherCards from './tabs/weatherCards';
import styles from '../css/App.css';

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
              {/* { <Add selectedMonth={this.state.selectedMonth} selectedYear={this.state.selectedYear} />}*/}
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
}
