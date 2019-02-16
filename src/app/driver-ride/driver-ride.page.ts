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
    listOfPassengers: any;
    reachedHSLoc: Boolean = false;
    hotSpotName: any;
    enoughPassenger: Boolean = false;

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
    const result = await this.getNearestHotSpot();
      if (result['success']) {
        this.assignedHotspot = result['assignedHotspot'];
        this.assignedZone = result['assignedZone'];
        this.hotSpots = result['hotspotLists'];
        this.hotSpotName = this.assignedHotspot.name;
        this.setSourceAddress();
      this.getDirections();
      }
      this.presentToast('Thanks for your availability, please go to the nearest hotspot marked in the map.');
    } else {
      this.reachedHSLoc = false;
      this.currentOrHotSpotLocation = 'Current Location';
      this.setSourceAddress();
      const message = 'Thanks for your service, you have earned ' + this.driverWalletBal + ' today.';
      this.presentToast(message);
    }
  }

  async presentToast(message) {
    const toast = await this.toastCtrl.create({
      message: message,
      duration: 5000
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
    const url = 'http://autofly.us-east-2.elasticbeanstalk.com/autofly/findHotspotZone';
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
    const result = await this.getAddress(this.sourceLocation.lat, this.sourceLocation.lng);
    this.sourceLocation.name = result['results'][0].formatted_address;
  }
  async reachedHotSpot() {
    this.currentOrHotSpotLocation =  'HotSpot Location';
    this.sourceLocation.lat = this.assignedHotspot.lat;
    this.sourceLocation.lng = this.assignedHotspot.lng;
    this.reachedHSLoc = true;
    this.setSourceAddress();
    const result = await await this.assignAuto();
    if (result) {
      this.reachedHSLoc = true;
    }
  }
  assignAuto() {
    const requestParam = {
      driverId: this.driverId,
      assignedHotspot: this.assignedHotspot.id,
      available: true,
      assignedZone: this.assignedZone,
    };
    const method = 'POST';
    const url = 'http://autofly.us-east-2.elasticbeanstalk.com/autofly/assignAuto';
    return this.commonAPICall(url, requestParam,  method);
  }

  async startTrip() {
   const result = await this.tripStarted();
    if (result['success']) {
      this.enoughPassenger = true;
      this.listOfPassengers = result['rides'].map(person => ({ rideId: person.rideId, toHotspot: person.toHotspot,
        passengerId: person.passengerId,
        toHotspotName: person.toHotspotName, passengerName: person.passengerName, zoneId: person.zoneId}));
    }  else {
      this.presentToast('Please wait for other passengers to board.');
    }

  }

  async endRide(passenger) {
    this.listOfPassengers = this.listOfPassengers.filter((item) => item.passengerId !== passenger.passengerId);
    const result =  await this.rideEndForPassanger(passenger);
    if (result['success']) {
      const msg = 'You have earned Rs.' + result['earning'] + ' from this ride.';
      this.presentToast(msg);
    }
  }
  async rideEndForPassanger(passenger) {
    const requestParam = {
        'driverId': this.driverId,
        'currentHotspotId': passenger.toHotspot,
        'zoneId': passenger.zoneId,
        'passengerId': passenger.passengerId,
        'rideId': passenger.rideId
    };
    const method = 'POST';
      const url = 'hhttp://autofly.us-east-2.elasticbeanstalk.com/autofly/endRide';
    return this.commonAPICall(url, requestParam,  method);
  }

  tripStarted() {
    const method = 'POST';
      const url = 'http://autofly.us-east-2.elasticbeanstalk.com/autofly/startRide';
      const requestParam = {
        'tripId': this.tripId,
        'driverId': this.driverId,
        'zoneId': this.assignedZone,
        'fromHotspotId': this.assignedHotspot.id
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
        request.setRequestHeader('Content-Type', 'application/json');
        request.send(JSON.stringify(requestParams));
      } else {
        request.send();
      }
    });
  }
}
