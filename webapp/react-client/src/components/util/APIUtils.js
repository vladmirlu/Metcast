import { API_BASE_URL, ACCESS_TOKEN,  ACCESS_TOKEN_PREFIX} from '../constants';

const request = (options) => {
    const headers = new Headers({
        'Content-Type': 'application/json',
    });
    if(localStorage.getItem(ACCESS_TOKEN)) {
        headers.append('Authorization', ACCESS_TOKEN_PREFIX + localStorage.getItem(ACCESS_TOKEN))
    }
    const defaults = {headers: headers};
    options = Object.assign({}, defaults, options);

    return fetch(options.url, options)
    .then(response => 
        response.json().then(json => {
            if(!response.ok) {
                return Promise.reject(json);
            }
            return json;
        })
    );
};

export function login(loginRequest) {

    return request({
        url: API_BASE_URL + "/auth/signin",
        method: 'POST',
        body: JSON.stringify(loginRequest)
    });
}

export function signup(signupRequest) {

    return request({
        url: API_BASE_URL + "/auth/signup",
        method: 'POST',
        body: JSON.stringify(signupRequest)
    });
}

export function getCurrentUser() {

    if(!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }
    return request({
        url: API_BASE_URL + "/users/current",
        method: 'GET'
    });
}

export function getUserProfile(username) {

    return request({
        url: API_BASE_URL + "/users/" + username,
        method: 'GET'
    });
}

export function getUserCreatedWeatherCards(username) {

    return request({
        url: API_BASE_URL + "/weather/cards/" + username,
        method: 'GET'
    });
}

export function adjustWeatherCardList(state) {

    return request({
        url: API_BASE_URL + "/weather/cards/" + state.username,
        method: 'POST',
        body: JSON.stringify(state.locations)
    });
}

export function deleteWeatherCard(location, username) {

    return request({
        url: API_BASE_URL + "/weather/cards/" + location + "/" + username,
        method: 'DELETE'
    });
}
