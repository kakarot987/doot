import { TestBed } from '@angular/core/testing';

import { HelperInterceptor } from './helper.interceptor';

describe('HelperInterceptor', () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      HelperInterceptor
      ]
  }));

  it('should be created', () => {
    const interceptor: HelperInterceptor = TestBed.inject(HelperInterceptor);
    expect(interceptor).toBeTruthy();
  });
});
