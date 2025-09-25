import { Routes } from '@angular/router';
import { BookListComponent } from './components/book-list/book-list.component';
import { AddBookComponent } from './components/add-book/add-book.component';
import { UpdateBookComponent } from './components/update-book/update-book.component';
import { AddUserComponent } from './components/add-user/add-user.component';
import { LoginUserComponent } from './components/login-user/login-user.component';
import { ShowUserComponent } from './components/show-user/show-user.component';
import { ViewMyProfileComponent } from './components/view-myprofile/view-myprofile.component';
import { ViewProfileComponent } from './components/view-profile/view-profile.component';

export const routes: Routes = [
    { path: '', component: LoginUserComponent},
    { path: 'books', component: BookListComponent},
    { path: 'add-book', component: AddBookComponent},
    { path: 'update-book/:id', component: UpdateBookComponent},
    { path: 'register', component: AddUserComponent},
    { path: 'show-user', component: ShowUserComponent},
    { path: 'view-myprofile', component: ViewMyProfileComponent},
    { path: 'view-profile', component: ViewProfileComponent}
];
