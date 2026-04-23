import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import CourseList from './CourseList';
import { CourseService } from '../services/CourseService';
import { StudentService } from '../services/StudentService';
import { describe, test, expect, beforeEach, vi, Mock } from 'vitest';

// 1. Mock the external service dependencies completely
vi.mock('../services/CourseService');
vi.mock('../services/StudentService');

describe('CourseList Component', () => {
    // Setup some fake data to return from our mocked services
    const mockCourses = [
        { id: 1, name: 'Intro to React', instructor: 101, maxSize: 25, room: 'Room A', roster: ['Alice'] },
        { id: 2, name: 'Advanced Java', instructor: 102, maxSize: 30, room: 'Room B', roster: [] }
    ];

    const mockStudents = [
        { id: 50, name: 'Alice' },
        { id: 51, name: 'Bob' }
    ];

    beforeEach(() => {
        // Clear previous mock calls before each test
        vi.clearAllMocks();

        // Tell our mock services what to return when the component calls them
        (CourseService.getAllCourses as Mock).mockResolvedValue(mockCourses);
        (StudentService.getAllStudents as Mock).mockResolvedValue(mockStudents);
    });

    test('renders loading state initially, then displays the course table', async () => {
        render(<CourseList />);

        // Verify the loading screen shows immediately
        expect(screen.getByText('Loading courses from Spring Boot...')).toBeInTheDocument();

        // waitFor pauses the test until the async useEffect finishes fetching data
        await waitFor(() => {
            expect(screen.queryByText('Loading courses from Spring Boot...')).not.toBeInTheDocument();
        });

        // Verify our mock data rendered in the table
        expect(screen.getByText('Intro to React')).toBeInTheDocument();
        expect(screen.getByText('Advanced Java')).toBeInTheDocument();
    });

    test('allows a user to add a new course', async () => {
        // Setup mock for the addCourse method
        (CourseService.addCourse as Mock).mockResolvedValue({});

        render(<CourseList />);
        await waitFor(() => expect(screen.queryByText('Loading courses from Spring Boot...')).not.toBeInTheDocument());

        // Simulate a user typing into the new course text inputs
        fireEvent.change(screen.getByPlaceholderText('New Course Name'), { target: { value: 'Biology 101' } });
        fireEvent.change(screen.getByPlaceholderText('Instructor ID Number'), { target: { value: '99' } });
        fireEvent.change(screen.getByPlaceholderText('MaxSize'), { target: { value: '40' } });
        fireEvent.change(screen.getByPlaceholderText('Room'), { target: { value: 'Lab 1' } });

        // Click the Add Course button
        const addButton = screen.getByRole('button', { name: /Add Course/i });
        fireEvent.click(addButton);

        // Verify the component passed the correct parsed data to the Service
        await waitFor(() => {
            expect(CourseService.addCourse).toHaveBeenCalledWith({
                name: 'Biology 101',
                instructor: 99, // Component should parse this to a number
                maxSize: 40,    // Component should parse this to a number
                room: 'Lab 1',
                roster: undefined
            });
        });
    });

    test('allows a user to delete a course', async () => {
        (CourseService.deleteCourse as Mock).mockResolvedValue({});

        render(<CourseList />);
        await waitFor(() => expect(screen.queryByText('Loading courses from Spring Boot...')).not.toBeInTheDocument());

        // Find all delete buttons on the screen (since there are multiple rows)
        const deleteButtons = screen.getAllByRole('button', { name: /Delete/i });

        // Click the first one (Intro to React, ID: 1)
        fireEvent.click(deleteButtons[0]);

        // Verify the service was called with ID 1
        await waitFor(() => {
            expect(CourseService.deleteCourse).toHaveBeenCalledWith(1);
        });
    });

    test('allows a user to edit a course', async () => {
        (CourseService.updateCourse as Mock).mockResolvedValue({});

        render(<CourseList />);
        await waitFor(() => expect(screen.queryByText('Loading courses from Spring Boot...')).not.toBeInTheDocument());

        // Click the Edit button for the first row
        const editButtons = screen.getAllByRole('button', { name: /Edit/i });
        fireEvent.click(editButtons[0]);

        // The edit row should now be visible. Change the course name.
        const nameInput = screen.getByPlaceholderText('Course Name');
        fireEvent.change(nameInput, { target: { value: 'Intro to React Native' } });

        // Click Save
        const saveButton = screen.getByRole('button', { name: /Save/i });
        fireEvent.click(saveButton);

        // Verify the update service was called with the modified data
        await waitFor(() => {
            expect(CourseService.updateCourse).toHaveBeenCalledWith(1, {
                name: 'Intro to React Native',
                instructor: 101,
                maxSize: 25,
                room: 'Room A'
            });
        });
    });

    test('allows a user to add a student to a course', async () => {
        (CourseService.addStudentToCourse as Mock).mockResolvedValue({});

        render(<CourseList />);
        await waitFor(() => expect(screen.queryByText('Loading courses from Spring Boot...')).not.toBeInTheDocument());

        // Find the dropdown for the second course row (Advanced Java)
        // Since you used generic IDs, we'll target it by its generic role and pick the second one
        const selects = screen.getAllByRole('combobox');

        // Simulate selecting "Bob" (ID: 51) from the dropdown for the second course
        fireEvent.change(selects[1], { target: { value: '51' } });

        // Click the Add Student button for that specific row
        const addStudentButtons = screen.getAllByRole('button', { name: /Add Student/i });
        fireEvent.click(addStudentButtons[1]);

        // Verify the correct Course ID (2) and Student ID (51) were sent to the backend
        await waitFor(() => {
            expect(CourseService.addStudentToCourse).toHaveBeenCalledWith(2, 51);
        });
    });
});