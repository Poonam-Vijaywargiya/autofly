import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-driver',
  templateUrl: './driver.page.html',
  styleUrls: ['./driver.page.scss'],
})
export class DriverPage implements OnInit {

  constructor(private router: Router) { }

  ngOnInit() {
  }
  public onClickCancel() {
    this.router.navigate(['/tabs/tab1']);
  }
}
