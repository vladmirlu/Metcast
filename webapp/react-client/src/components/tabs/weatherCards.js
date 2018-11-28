import React from 'react';
import ReactDOM from 'react-dom';
import { Tab, Tabs } from 'react-bootstrap'
import WeatherCardRouter from './weatherCardRouter'

class WeatherCards extends React.Component {
 constructor(){
  super();
  this.state = {activeTab:''};
  this.handleSelect = this.handleSelect.bind(this);
}
componentWillReceiveProps(nextProps) {
    this.setState({activeTab:this.props.year+'-'+nextProps.monthlyActiveTab});
  }
handleSelect(selectedTab) {
     this.setState({
       activeTab: selectedTab
     });
 }
render(){
  return <Tabs activeKey={this.state.activeTab} onSelect={this.handleSelect}>
            <Tab eventKey={this.props.year+'-All'} title={<WeatherCardRouter tabId='All' year={this.props.year}/>}></Tab>

    </Tabs>
}
}
export default WeatherCards;
