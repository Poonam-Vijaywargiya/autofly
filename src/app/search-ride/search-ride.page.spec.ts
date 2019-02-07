import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchRidePage } from './search-ride.page';

describe('SearchRidePage', () => {
  let component: SearchRidePage;
  let fixture: ComponentFixture<SearchRidePage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SearchRidePage ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SearchRidePage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
