import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CardCreationComponent } from './card-creation.component';

describe('CardCreationComponent', () => {
  let component: CardCreationComponent;
  let fixture: ComponentFixture<CardCreationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CardCreationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CardCreationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
