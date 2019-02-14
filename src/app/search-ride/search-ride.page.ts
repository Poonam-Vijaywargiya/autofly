import { Component, ElementRef, NgZone, ViewChild } from '@angular/core';
import { Geolocation } from '@ionic-native/geolocation/ngx';
import { Router, ActivatedRoute } from '@angular/router';
import { NativeStorage } from '@ionic-native/native-storage/ngx';
import { GooglePlus } from '@ionic-native/google-plus/ngx';
import { LoadingController, ToastController } from '@ionic/angular';

declare var google: any;
@Component({
  selector: 'app-search-ride',
  templateUrl: './search-ride.page.html',
  styleUrls: ['./search-ride.page.scss'],
})
export class SearchRidePage {

  constructor(public zone: NgZone, public geolocation: Geolocation, private googlePlus: GooglePlus,
    private nativeStorage: NativeStorage,
    public loadingController: LoadingController,
    private router: Router,
    private route: ActivatedRoute,
    public toastCtrl: ToastController) {
    /*load google map script dynamically */
    const script = document.createElement('script');

    script.id = 'googleMap';
    this.userId = this.route.snapshot.paramMap.get('userId');
    this.walletBal = this.route.snapshot.paramMap.get('walletBal');
    if (this.apiKey) {
      script.src = 'https://maps.googleapis.com/maps/api/js?key=' + this.apiKey + '&libraries=places';
    } else {
      script.src = 'https://maps.googleapis.com/maps/api/js?key=';
    }
    document.head.appendChild(script);
    /*Get Current location*/

    this.geolocation.getCurrentPosition({ enableHighAccuracy: false }).then((position) => {
      this.sourceLocation.lat = position.coords.latitude;
      this.sourceLocation.lng = position.coords.longitude;
      this.map = new google.maps.Map(this.mapElement.nativeElement, this.mapOptions);
      this.setSourceAddress();
    });
    /*Map options*/
    this.mapOptions = {
      center: this.sourceLocation,
      zoom: 15,
      mapTypeControl: false
    };
  }
  @ViewChild('Map') mapElement: ElementRef;
  map: any;
  mapOptions: any;
  sourceLocation = { lat: null, lng: null, name: null };
  destinationLocation = { lat: null, lng: null, name: null };
  apiKey: any = 'AIzaSyCtPkZ9pSU34VXWGihx_i4Ca4HgL4puVJ0'; /*Your API Key*/ // 'AIzaSyCtPkZ9pSU34VXWGihx_i4Ca4HgL4puVJ0'
  autocompleteService: any;
  placesService: any;
  query: String = '';
  queryForSource: String = '';
  destiPlaces: any = [];
  sourcePlaces: any = [];
  searchDisabled: boolean;
  saveDisabled: boolean;
  selectLocation: any;
  icons = {};
  fareAmount: number;
  isenabled: Boolean = false;
  isRideStarted: Boolean = false;
  walletBal: any;
  userId: any;
  hotSpots: any;
  autoNumber: any;
  addMoney: Boolean = false;
  tripId: any;
  // Method to get search places and form autocomplete list
  searchPlace(val) {
    let config;
    this.saveDisabled = true;
    if (this.query.length > 0 || this.sourceLocation.name.length > 0) {
      if (val === 'source') {
        config = {
          types: ['geocode'],
          input: this.sourceLocation.name
        };
        this.autocompleteService.getPlacePredictions(config, (predictions, status) => {
          if (status === google.maps.places.PlacesServiceStatus.OK && predictions) {
            this.sourcePlaces = [];
            predictions.forEach((prediction) => {
              this.sourcePlaces.push(prediction);
            });
          }
        });
      } else {
        config = {
          types: ['geocode'],
          input: this.query
        };
        this.autocompleteService.getPlacePredictions(config, (predictions, status) => {
          if (status === google.maps.places.PlacesServiceStatus.OK && predictions) {
            this.destiPlaces = [];
            predictions.forEach((prediction) => {
              this.destiPlaces.push(prediction);
            });
          }
        });
      }

    } else {
      this.destiPlaces = []; this.sourcePlaces = [];
      this.sourceLocation = { lat: null, lng: null, name: null };
      this.destinationLocation = { lat: null, lng: null, name: null };
    }
  }

  // Method to select specific place from the list and  get place details.
  selectPlace(place, val) {
    this.sourcePlaces = [];
    this.destiPlaces = [];
    const selectLocation = {
      lat: null,
      lng: null,
      name: place.name
    };
    if (val === 'source') {
      this.sourceLocation.name = place.description;
      this.placesService.getDetails({ placeId: place.place_id }, (details) => {
        this.zone.run(() => {
          selectLocation.name = details.name;
          selectLocation.lat = details.geometry.location.lat();
          selectLocation.lng = details.geometry.location.lng();
          this.saveDisabled = false;
          this.sourceLocation = selectLocation;
          // if (this.destinationLocation) {
          //   this.getDirections();
          // }
        });
      });
    } else {
      this.query = place.description;
      this.placesService.getDetails({ placeId: place.place_id }, (details) => {
        this.zone.run(() => {
          selectLocation.name = details.name;
          selectLocation.lat = details.geometry.location.lat();
          selectLocation.lng = details.geometry.location.lng();
          this.saveDisabled = false;
          this.destinationLocation = selectLocation;
          // if (this.sourceLocation) {
          //   this.getDirections();
          // }
        });
      });
    }
  }

  // Method to route rendered on the map
  getDirections() {
    const directionsService = new google.maps.DirectionsService;
    this.map = new google.maps.Map(this.mapElement.nativeElement, this.mapOptions);
    const directionsDisplayW1 = new google.maps.DirectionsRenderer({
      suppressMarkers: true,
      map: this.map,
      preserveViewport: true
    });
    const directionsDisplayW2 = new google.maps.DirectionsRenderer({
      suppressMarkers: true,
      map: this.map,
      preserveViewport: true
    });
    const directionsDisplayD = new google.maps.DirectionsRenderer;
    directionsDisplayD.setMap(this.map);
    this.calculateAndDisplayRoute(directionsService, directionsDisplayD, directionsDisplayW1, directionsDisplayW2);
  }

  // Utility method to convert object of lat and lng to single comma separated string to be sent to the google api
  convertObjectToString(val) {
    let str = '';
    str = val.lat + ', ' + val.lng;
    return str;
  }

  async presentLoading(loading) {
    return await loading.present();
  }

  // Display route on the map based on the hotspots receive from the backend
  async calculateAndDisplayRoute(directionsService, directionsDisplayD,  directionsDisplayW1, directionsDisplayW2, ) {
    const waypts = [];
    const loading = await this.loadingController.create({
      message: 'Please wait...'
    });
    this.presentLoading(loading);
    this.hotSpots = {
      'source': {
        'lat': 12.98747,
        'lng': 77.736464
      },
      'destination': {
        'lat': 12.997361,
        'lng': 77.69663
      },
      'passengerId': 1,
      'departureTime': '2019-02-13T16:49:22.505',
      'route': [
        {
          'id': 1,
          'name': 'ITPL Mall',
          'lat': 12.98747,
          'lng': 77.736464,
          'dropLocation': false
        },
        {
          'id': 2,
          'name': 'PSN',
          'lat': 12.98957,
          'lng': 77.727983,
          'dropLocation': false
        },
        {
          'id': 3,
          'name': 'Hoodi Circle',
          'lat': 12.992353,
          'lng': 77.716387,
          'dropLocation': true
        },
        {
          'id': 4,
          'name': 'Brigade Metropolis',
          'lat': 12.993053,
          'lng': 77.703638,
          'dropLocation': false
        },
        {
          'id': 5,
          'name': 'Phoenix Mall',
          'lat': 12.997361,
          'lng': 77.69663,
          'dropLocation': false
        }
      ],
      'success': true,
      'walkFromSource': [
        {
          'lat': 12.992353,
          'lng': 77.716387,
        },
        {
          'lat': 12.98747,
          'lng': 77.736464
        }
      ],
      'walkToDestination': [
        {
          'lat': 12.997361,
          'lng': 77.69663
        },
        {
          'lat': 12.997361,
          'lng': 77.69663
        }
      ]
    };
    const lineSymbol = {
      path: google.maps.SymbolPath.CIRCLE,
      strokeOpacity: 1,
      scale: 2
    };
    // check if source to first hotspot you need to walk or not
    if (this.hotSpots.walkFromSource && (this.hotSpots.walkFromSource[0].lng !== this.hotSpots.walkFromSource[1].lng)
     && (this.hotSpots.walkFromSource[0].lat !== this.hotSpots.walkFromSource[1].lat)) {
      await directionsService.route({
        origin: this.convertObjectToString(this.hotSpots.walkFromSource[0]),
        destination: this.convertObjectToString(this.hotSpots.walkFromSource[1]),
        waypoints: [],
        optimizeWaypoints: true,
        travelMode: 'WALKING'
      }, function (response, status) {
        if (status === 'OK') {
          directionsDisplayW1.setDirections(response);
          directionsDisplayW1.setOptions({
            polylineOptions: {
              strokeWeight: 4,
              strokeOpacity: 0,
              strokeColor: 'green',
              icons: [{
                icon: lineSymbol,
                offset: '0',
                repeat: '20px'
              }]
            }
          });
        } else {
          window.alert('Directions request failed due to ' + status);
        } loading.dismiss();
      });
    }
    // check from last spot do you need to walk to the destination or not
    if (this.hotSpots.walkToDestination && (this.hotSpots.walkToDestination[0].lng !== this.hotSpots.walkToDestination[1].lng)
    && (this.hotSpots.walkToDestination[0].lat !== this.hotSpots.walkToDestination[1].lat)) {
      await directionsService.route({
        origin: this.convertObjectToString(this.hotSpots.walkToDestination[0]),
        destination: this.convertObjectToString(this.hotSpots.walkToDestination[1]),
        waypoints: [],
        optimizeWaypoints: true,
        travelMode: 'WALKING'
      }, function (response, status) {
        if (status === 'OK') {
          directionsDisplayW2.setDirections(response);
          directionsDisplayW2.setOptions({
            polylineOptions: {
              strokeWeight: 4,
              strokeOpacity: 0,
              strokeColor: 'red',
              icons: [{
                icon: lineSymbol,
                offset: '0',
                repeat: '20px'
              }]
            }
          });
        } else {
          window.alert('Directions request failed due to ' + status);
        }
      });
    }

       // mark the hotspots in the map
    if (this.hotSpots.route) {
      this.hotSpots.route.forEach((element, i) => {
        if (i > 0 && i < this.hotSpots.route.length) {
          waypts.push({
            location: this.convertObjectToString(element),
            stopover: true
          });
        }
      });
      await directionsService.route({
        origin: this.convertObjectToString(this.hotSpots.route[0]),
        destination: this.convertObjectToString(this.hotSpots.route[this.hotSpots.route.length - 1]),
        waypoints: waypts,
        optimizeWaypoints: true,
        travelMode: 'DRIVING'
      }, function (response, status) {
        if (status === 'OK') {
          directionsDisplayD.setDirections(response);
        } else {
          window.alert('Directions request failed due to ' + status);
        }
        loading.dismiss();
      });
    }
  }

  // Method is called to redirect the page to home page.
  public onClickCancel() {
    this.router.navigate(['/tabs/tab1']);
  }

  // Method to call google address api to the fetch the address
  getAddress(lat, lng) {
    return new Promise(function (resolve, reject) {
      const request = new XMLHttpRequest();
      const method = 'GET';
      const url = 'https://maps.googleapis.com/maps/api/geocode/json?latlng=' + lat + ',' + lng +
        '&sensor=true&key=AIzaSyCtPkZ9pSU34VXWGihx_i4Ca4HgL4puVJ0';
      const async = true;

      request.open(method, url, async);
      request.onreadystatechange = function () {
        if (request.readyState === 4) {
          if (request.status === 200) {
            const data = JSON.parse(request.responseText);
            const address = data.results[0].formatted_address;
            resolve(address);
          } else {
            reject(request.status);
          }
        }
      };
      request.send();
    });
  }
  // Method to set the address for source fetched from gps to the source location box.
  async setSourceAddress() {
    // await code here
    const result = await this.getAddress(this.sourceLocation.lat, this.sourceLocation.lng);
    // code below here will only execute when await makeRequest() finished loading
    this.sourceLocation.name = result;
    this.autocompleteService = new google.maps.places.AutocompleteService();
    this.placesService = new google.maps.places.PlacesService(this.map);
    // this.searchDisabled = false;
  }

  async bookRide() {
    // click book ride is called and i need to get the address now with the route map
    if (this.sourceLocation.name && this.destinationLocation.name) {
      //  this.hotSpots = await this.getHotSpots();
      this.fareAmount = 990; // response returned from service
      this.isenabled = true;
      this.getDirections();
    }
  }

  // Method which gives the start, end and way points and fare details
  getHotSpots() {
    return new Promise(function (resolve, reject) {
      const request = new XMLHttpRequest();
      const method = 'POST';
      const url = 'http://localhost:8181/autofly/getRoute';
      const async = true;
      const currentTime = new Date().toISOString();
      const routeDetails = {
        'source': this.sourceLocation,
        'destination': this.destinationLocation,
        'passengerId': this.userId,
        'departureTime': currentTime
      };
      request.open(method, url, async);
      request.setRequestHeader('Content-Type', 'application/json');
      request.onreadystatechange = function () {
        if (request.readyState === 4) {
          if (request.status === 200) {
            const response = JSON.parse(request.responseText);
            resolve(response.success);
          } else {
            reject(request.status);
          }
        }
      };
      request.send(JSON.stringify(routeDetails));
    });

  }

  async confirmRide() {
    if (this.walletBal < this.fareAmount) {
      this.presentToast();
      this.addMoney = true;
    } else {
      // const autoDetails = await this.getAutoDetails();
      this.autoNumber =  'ABCDEFG'; // autoDetails.autoNumber;
      this.isRideStarted = true;
      this.tripId = 'eiduieua'; // this.getAutoDetails.tripId;
    }
    // send api call that user is ready to board
  }
  getAutoDetails() {
    return new Promise(function (resolve, reject) {
      const request = new XMLHttpRequest();
      const method = 'POST';
      const url = 'http://localhost:8181/autofly/getRoute'; // change url
      const async = true;
      const currentTime = new Date().toISOString();
      const hotSpotsDetails = this.hotspots ;
      request.open(method, url, async);
      request.setRequestHeader('Content-Type', 'application/json');
      request.onreadystatechange = function () {
        if (request.readyState === 4) {
          if (request.status === 200) {
            const response = JSON.parse(request.responseText);
            resolve(response.success);
          } else {
            reject(request.status);
          }
        }
      };
      request.send(JSON.stringify(hotSpotsDetails));
    });
  }

  async joinRide() {
    // send api call that user boarded the auto to increase the count
 // userid and trip id
    const rideJoined = await this.rideJoined();
  }

  rideJoined() {
    const rideDetails = {
      'tripId': this.tripId,
      'passengerId': this.userId
    };
    return new Promise(function (resolve, reject) {
      const request = new XMLHttpRequest();
      const method = 'POST';
      const url = 'http://localhost:8181/autofly/getRoute'; // change url
      const async = true;
      const currentTime = new Date().toISOString();
      request.open(method, url, async);
      request.setRequestHeader('Content-Type', 'application/json');
      request.onreadystatechange = function () {
        if (request.readyState === 4) {
          if (request.status === 200) {
            const response = JSON.parse(request.responseText);
            resolve(response.success);
          } else {
            reject(request.status);
          }
        }
      };
      request.send(JSON.stringify(rideDetails));
    });
  }
  async presentToast() {
    const toast = await this.toastCtrl.create({
      message: 'Sorry! you have insufficient balance. Please add balance',
      duration: 2000
    });
    toast.present();
  }
  // based on hot spot have to write the logic to fetch source destination and walkable distance cordinate.
}
