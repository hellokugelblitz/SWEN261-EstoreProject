import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, of, tap } from 'rxjs';
import { MessageService } from './message.service';
import { Product } from './product';
import { User } from './user';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private usersUrl = "http://localhost:8080/users"
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };
  private activeUser: string = "";
  private loggedIn = false;
  private currentUser: User | undefined;

  constructor(private messageService: MessageService, private http: HttpClient) { }

  setUser(username: string) {
    if (this.activeUser == "" && this.loggedIn == false) {
      this.activeUser = username
      this.loggedIn = true

      this.getCurrentUserRequest()?.subscribe(user => this.currentUser = user)
    }
  }

  logout() {
    this.activeUser = ""
    this.loggedIn = false
  }

  isLoggedIn() {
    return this.loggedIn
  }

  getCurrentUser(): User | undefined {
    return this.currentUser
  }

  getCurrentUserRequest(): Observable<User> | null {
    if (this.isLoggedIn() == false) {
      return null;
    } else {
      let username = this.activeUser
      const url = `${this.usersUrl}/${username}`;

      return this.http.get<User>(url).pipe(
        tap(_ => this.log(`fetched user=${username}`)),
        catchError(this.handleError<User>(`getCurrentUser id=${username}`))
      );
    }
  }

  addToShoppingCart(product: Product): boolean {
    console.log(product, this.isLoggedIn())
    if (this.isLoggedIn()) {
      const url = `${this.usersUrl}/shoppingCart/${this.activeUser}`;

      this.http.put(url, product, this.httpOptions).pipe(
        tap(_ => this.log(`added product id=${product.id} to cart`)),
        catchError(this.handleError<any>('addToShoppingCart'))
      ).subscribe(user => this.currentUser = user);

      return true;
    }

    return false;
  }

  removeFromShoppingCart(product: Product): boolean {
    if (this.isLoggedIn()) {
      const url = `${this.usersUrl}/shoppingCart/${this.activeUser}`;

      this.http.post(url, product, this.httpOptions).pipe(
        tap(_ => this.log(`removed product id=${product.id} from cart`)),
        catchError(this.handleError<any>('removeFromShoppingCart'))
      ).subscribe(user => this.currentUser = user);

      return true;
    }

    return false;
  }

  /**
   * Handle Http operation that failed.
   * Let the app continue.
   *
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // TODO: better job of transforming error for user consumption
      this.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

  /** Log a UserSevice message with the MessageService */
  private log(message: string) {
    this.messageService.add(`UserService: ${message}`);
  }
}
