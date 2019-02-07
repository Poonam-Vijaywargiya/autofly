import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PassengerPage } from './passenger.page';

describe('PassengerPage', () => {
  let component: PassengerPage;
  let fixture: ComponentFixture<PassengerPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PassengerPage ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PassengerPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
