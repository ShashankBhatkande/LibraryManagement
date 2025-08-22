import { Routes } from '@angular/router';
import { BookListComponent } from './components/book-list/book-list.component';
import { AddBookComponent } from './components/add-book/add-book.component';
import { updateBookComponent } from './components/update-book/update-book.component';
import { addUserComponent } from './components/add-user/add-user.component';

export const routes: Routes = [
    { path: '', component: BookListComponent},
    { path: 'add-book', component: AddBookComponent},
    { path: 'update-book/:id', component: updateBookComponent},
    { path: 'add-user', component: addUserComponent}
];
