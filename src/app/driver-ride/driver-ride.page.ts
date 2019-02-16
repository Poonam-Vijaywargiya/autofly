import { LoadingController, ToastController } from '@ionic/angular';
import { Router, ActivatedRoute } from '@angular/router';
import { Component, ElementRef, NgZone, ViewChild } from '@angular/core';
import { Geolocation } from '@ionic-native/geolocation/ngx';
import { NativeStorage } from '@ionic-native/native-storage/ngx';
import { GooglePlus } from '@ionic-native/google-plus/ngx';
declare var google: any;
@Component({
  selector: 'app-driver-ride',
  templateUrl: './driver-ride.page.html',
  styleUrls: ['./driver-ride.page.scss'],
})

export class DriverRidePage {

  constructor(public zone: NgZone, public geolocation: Geolocation, private googlePlus: GooglePlus,
    public loadingController: LoadingController,
    private router: Router,
    private route: ActivatedRoute,
    public toastCtrl: ToastController) { /*load google map script dynamically */
      const script = document.createElement('script');
      script.id = 'googleMap';
      this.driverId = this.route.snapshot.paramMap.get('driverId');
      this.driverWalletBal = Number(this.route.snapshot.paramMap.get('driverWalletBal'));
      if (this.apiKey) {
        script.src = 'https://maps.googleapis.com/maps/api/js?key=' + this.apiKey + '&libraries=places';
      } else {
        script.src = 'https://maps.googleapis.com/maps/api/js?key=';
      }
      document.head.appendChild(script);
      /*Get Current location*/
      this.geolocation.getCurrentPosition({ enableHighAccuracy: false }).then((position) => {
        this.sourceLocation.lat = this.driverLat = position.coords.latitude;
        this.sourceLocation.lng = this.driverLng = position.coords.longitude;
        this.map = new google.maps.Map(this.mapElement.nativeElement, this.mapOptions);

        const marker = new google.maps.Marker({
          position: this.sourceLocation,
          map: this.map,
          draggable: true,
          animation: google.maps.Animation.DROP,
          title: 'Current Location',
          label: 'A'
        });

        this.currentOrHotSpotLocation = 'Current Location';
        this.setSourceAddress();
      });
      /*Map options*/
      this.mapOptions = {
        center: this.sourceLocation,
        zoom: 15,
        mapTypeControl: false
      };
    }
    @ViewChild('DriverMap') mapElement: ElementRef;
    map: any;
    mapOptions: any;
    sourceLocation = { lat: null, lng: null, name: null };
    destinationLocation = { lat: null, lng: null, name: null };
    apiKey: any = 'AIzaSyCtPkZ9pSU34VXWGihx_i4Ca4HgL4puVJ0'; /*Your API Key*/ // 'AIzaSyCtPkZ9pSU34VXWGihx_i4Ca4HgL4puVJ0'
    icons = {};
    fareAmount: number;
    isenabled: Boolean = false;
    isRideStarted: Boolean = false;
    driverWalletBal = 0;
    driverId: any;
    hotSpots: any;
    autoNumber: any;
    addMoney: Boolean = false;
    tripId: any;
    isToggled: Boolean = false;
    currentOrHotSpotLocation: any;
    driverLat: any;
    driverLng: any;
    assignedZone: any;
    assignedHotspot: any;
    listOfPassangers: any;
    reachedHSLoc: Boolean = false;

  // Utility method to convert object of lat and lng to single comma separated string to be sent to the google api
  convertObjectToString(val) {
    let str = '';
    str = val.lat + ', ' + val.lng;
    return str;
  }

  async presentLoading(loading) {
    return await loading.present();
  }
  async getDriverLocation() {
    if (this.isToggled) {
  // call the api to send the current location and get nearest hotspot location
    // let result = await this.getNearestHotSpot();
     this.hotSpots = {
      'success': true,
      'assignedZone': 1,
      'assignedHotspot': {
          'id': 3,
          'name': 'Hoodi Circle-PSN',
          'lat': 12.992353,
          'lng': 77.716387
      },
      'hotspotLists': [
          {
              'id': 1,
              'name': 'ITPL Mall',
              'lat': 12.98747,
              'lng': 77.736464
          },
          {
              'id': 2,
              'name': 'PSN',
              'lat': 12.98957,
              'lng': 77.727983
          },
          {
              'id': 3,
              'name': 'Hoodi Circle-PSN',
              'lat': 12.992353,
              'lng': 77.716387
          }
      ]
  };
    const result = this.hotSpots;
      if (result.success) {
        this.assignedHotspot = result.assignedHotspot;
        this.assignedZone = result.assignedZone;
        this.hotSpots = result.hotspotLists;
        this.currentOrHotSpotLocation =  'HotSpot Location';
        this.setSourceAddress();
      this.getDirections();
      }
    } else {
      this.reachedHSLoc = false;
      this.currentOrHotSpotLocation = 'Current Location';
      this.setSourceAddress();
      // Do we have api to delete driver from queue then call that here.
    }
    this.presentToast(this.isToggled);
  }

  async presentToast(onDuty) {
    let message;
    if (onDuty) {
      message = 'Thanks for your availability, please go to the nearest hotspot marked in the map.';
    } else {
      message = 'Thanks for your service, you have earned ' + this.driverWalletBal + ' today.';
    }
    const toast = await this.toastCtrl.create({
      message: message,
      duration: 2000
    });
    toast.present();
  }

  getNearestHotSpot() {
    const requestParam = {
      'driverId' : this.driverId,
      'driverLat': this.driverLat,
      'driverLng': this.driverLng
    };
    const method = 'POST';
    const url = 'http://localhost:8181/autofly/findHotspotZone';
    return this.commonAPICall(url, requestParam,  method);
}

  // Method to route rendered on the map
  getDirections() {
    const directionsService = new google.maps.DirectionsService;
    this.map = new google.maps.Map(this.mapElement.nativeElement, this.mapOptions);
    const directionsDisplayD = new google.maps.DirectionsRenderer;
    directionsDisplayD.setMap(this.map);
    this.calculateAndDisplayRoute(directionsService, directionsDisplayD);
  }

  // Display route on the map based on the hotspots receive from the backend
  async calculateAndDisplayRoute(directionsService, directionsDisplayD) {
    const waypts = [];
    const loading = await this.loadingController.create({
      message: 'Please wait...'
    });
    this.presentLoading(loading);
       // mark the hotspots in the map
    if (this.hotSpots) {
      this.hotSpots.forEach((element, i) => {
        if (i = 0 && i < this.hotSpots.length) {
          if (this.convertObjectToString(element) !== this.convertObjectToString(this.assignedHotspot)) {
            waypts.push({
              location: this.convertObjectToString(element),
              stopover: true
            });
          }
        }
      });
      await directionsService.route({
        origin: this.convertObjectToString(this.sourceLocation),
        destination: this.convertObjectToString(this.assignedHotspot),
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
    const method = 'GET';
    const url = 'https://maps.googleapis.com/maps/api/geocode/json?latlng=' + lat + ',' + lng +
    '&sensor=true&key=AIzaSyCtPkZ9pSU34VXWGihx_i4Ca4HgL4puVJ0';
    return this.commonAPICall(url, null,  method);
  }

  // Method to set the address for source fetched from gps to the source location box.
  async setSourceAddress() {
    // await code here
    const result = await this.getAddress(this.sourceLocation.lat, this.sourceLocation.lng);
    // code below here will only execute when await makeRequest() finished loading
    this.sourceLocation.name = result['results'][0].formatted_address;
  }
  async reachedHotSpot() {
    this.listOfPassangers = {
      listOfPassangers:
     [{
        'id': 1,
        'name': 'Poonam'
    },
    {
        'id': 2,
        'name': 'Shohini'
    }]
    };
    this.reachedHSLoc = true;
    this.listOfPassangers = this.listOfPassangers.listOfPassangers;
    // const result = await await this.getPassangerList();
    // if (result.success) {
    //   this.listOfPassangers = result.listOfPassangers.listOfPassangers;
    // }
  }
  getPassangerList() {
    const requestParam = {
      driverId: this.driverId,
      assignedHotspot: this.assignedHotspot.id
    };
    const method = 'POST';
    const url = 'http://localhost:8181/autofly/getRoute'; // change url
    return this.commonAPICall(url, requestParam,  method);
  }

  async startTrip() {
    // send api call that user boarded the auto to increase the count
  // userid and trip id
  // what to do once driver has started the trip
    const tripStarted = await this.tripStarted();
  }

  async endRide(passangerId) {
    // const rideEndForPassanger = await this.rideEndForPassanger(passangerId);
    this.listOfPassangers = {
      listOfPassangers:
     [{
        'id': 1,
        'name': 'Poonam'
    }]
    };
    this.listOfPassangers = this.listOfPassangers.listOfPassangers;
    // const result = await await this.getPassangerList();
    // if (result.success) {
    //   this.listOfPassangers = result.listOfPassangers.listOfPassangers;
    // }
  }
  async rideEndForPassanger(pId) {
    const requestParam = {
      pId: pId,
      assignedHotspot: this.assignedHotspot.id
    };
    const method = 'POST';
      const url = 'http://localhost:8181/autofly/getRoute'; // change URL
    return this.commonAPICall(url, requestParam,  method);
  }

  tripStarted() {
    const method = 'POST';
      const url = 'http://localhost:8181/autofly/getRoute'; // change URL
      const requestParam = {
        'tripId': this.tripId,
        'driverId': this.driverId
      };
      return this.commonAPICall(url, requestParam,  method);
  }

  // common method to call api
  commonAPICall(url, requestParams, methodType) {
    return new Promise(function (resolve, reject) {
      const request = new XMLHttpRequest();
      const async = true;
      request.open(methodType, url, async);
      request.onreadystatechange = function () {
        if (request.readyState === 4) {
          if (request.status === 200) {
            const response = JSON.parse(request.responseText);
            resolve(response);
          } else {
            reject(request.status);
          }
        }
      };
      if (requestParams) {
        request.send(requestParams);
      } else {
        request.send();
      }
    });
  }
}
