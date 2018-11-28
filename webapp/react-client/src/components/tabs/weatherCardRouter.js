import React from 'react';
import ReactDOM from 'react-dom';
import { Link } from 'react-router-dom';
class WeatherCardRouter extends React.Component {
 constructor(){
  super();
  this.state={style:{'font-size': '10px'}}
 }
render(){
  if(this.props.tabId == 'All'){
   return <Link to={{pathname: '/', search: '?weatherCards=All'}} >
     <p style={this.state.style}>Show All</p>
    </Link>
  }
else if(this.props.tabId == 'One'){
   return <Link to={{pathname: '/', search: '?weatherCard=One'}} >
     <p style={this.state.style}>Show One</p>
    </Link>
  }
}
}
export default WeatherCardRouter;
