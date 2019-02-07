import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ThrowStmt } from '@angular/compiler';

@Component({
  selector: 'app-register',
  templateUrl: './register.page.html',
  styleUrls: ['./register.page.scss'],
})
export class RegisterPage implements OnInit {
  userDetails = {
    userName : '',
    password : '',
    reEnterPassword: ''
  };
  constructor(private router: Router) { }

  ngOnInit() {
  }

  public onClickCancel() {
    this.router.navigate(['/tabs/tab1']);
  }
  register() {
    if (this.userDetails.password !== this.userDetails.reEnterPassword) {
      alert('Passwords do not match');
      return;
    }
    console.log(this.userDetails);
  }
}
