import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DriverRidePage } from './driver-ride.page';

describe('DriverRidePage', () => {
  let component: DriverRidePage;
  let fixture: ComponentFixture<DriverRidePage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DriverRidePage ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DriverRidePage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
