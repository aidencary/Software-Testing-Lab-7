import React from 'react';
import { BrowserRouter, Routes, Route, Link } from 'react-router-dom';
import CourseList from './components/CourseList';
import NavBar from './components/NavBar';
import StudentList from "./components/StudentList";

import './App.css';

function App() {
    console.log("App is rendering!");
    return (
        <BrowserRouter>
        <div className="App">
            <nav className = "navbar">
                <h2 style={{ margin: 0 }}>My School App</h2>
                <div className= "nav-links">
                    {/* Use <Link> instead of <a> tags so the page doesn't hard-refresh */}
                    <Link id = "nav-course-list-link" to="/" className="nav-link">Courses</Link>
                    <Link id = "nav-student-list-link" to="/students" className="nav-link">Students</Link>
                </div>
            </nav>
            <div className = "main-content">
                <Routes>
                    {/* When the URL is "/", show CourseList */}
                    <Route path="/" element={<CourseList />} />

                    {/* When the URL is "/students", show StudentList */}
                    <Route path="/students" element={<StudentList />} />
                </Routes>
            </div>
        </div>
        </BrowserRouter>
    );
}

export default App;