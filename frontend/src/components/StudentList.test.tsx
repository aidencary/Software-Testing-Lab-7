import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { describe, test, expect, beforeEach, vi, Mock } from 'vitest';
import StudentList from './StudentList';
import { StudentService } from '../services/StudentService';

// 1. Mock the external StudentService
vi.mock('../services/StudentService');

describe('StudentList Component', () => {
    // Setup fake data to return from our mocked service
    const mockStudents = [
        { id: 1, name: 'John Doe', major: 'Computer Science', gpa: 3.8 },
        { id: 2, name: 'Jane Smith', major: 'Mathematics', gpa: 4.0 }
    ];

    beforeEach(() => {
        // Clear previous mock calls before each test
        vi.clearAllMocks();

        // Tell our mock service what to return when the component loads
        (StudentService.getAllStudents as Mock).mockResolvedValue(mockStudents);
    });

    test('renders loading state initially, then displays the student table', async () => {
        render(<StudentList />);

        // Note: Your component currently says "Loading courses from Spring Boot..."
        // We test for exactly what is in your code!
        expect(screen.getByText('Loading students from Spring Boot...')).toBeInTheDocument();

        // Wait for the async useEffect to finish fetching data
        await waitFor(() => {
            expect(screen.queryByText('Loading students from Spring Boot...')).not.toBeInTheDocument();
        });

        // Verify our mock data rendered in the table
        expect(screen.getByText('John Doe')).toBeInTheDocument();
        expect(screen.getByText('Mathematics')).toBeInTheDocument();
    });

    test('allows a user to add a new student', async () => {
        // Setup mock for the addStudent method
        (StudentService.addStudent as Mock).mockResolvedValue({});

        render(<StudentList />);
        await waitFor(() => expect(screen.queryByText('Loading courses from Spring Boot...')).not.toBeInTheDocument());

        // Simulate typing into the new student text inputs
        fireEvent.change(screen.getByPlaceholderText('New Student Name'), { target: { value: 'Alice Johnson' } });
        fireEvent.change(screen.getByPlaceholderText('New Student Major'), { target: { value: 'Physics' } });
        fireEvent.change(screen.getByPlaceholderText('New Student GPA'), { target: { value: '3.9' } });

        // Click the Add Student button
        const addButton = screen.getByRole('button', { name: /Add Student/i });
        fireEvent.click(addButton);

        // Verify the component passed the correct parsed data to the Service
        await waitFor(() => {
            expect(StudentService.addStudent).toHaveBeenCalledWith({
                name: 'Alice Johnson',
                major: 'Physics',
                gpa: 3.9 // Component should parse this to a float
            });
        });
    });

    test('allows a user to delete a student', async () => {
        (StudentService.deleteStudent as Mock).mockResolvedValue({});

        render(<StudentList />);
        await waitFor(() => expect(screen.queryByText('Loading courses from Spring Boot...')).not.toBeInTheDocument());

        // Find all delete buttons on the screen
        const deleteButtons = screen.getAllByRole('button', { name: /Delete/i });

        // Click the first one (John Doe, ID: 1)
        fireEvent.click(deleteButtons[0]);

        // Verify the service was called with ID 1
        await waitFor(() => {
            expect(StudentService.deleteStudent).toHaveBeenCalledWith(1);
        });
    });

    test('allows a user to edit a student', async () => {
        (StudentService.updateStudent as Mock).mockResolvedValue({});

        render(<StudentList />);
        await waitFor(() => expect(screen.queryByText('Loading courses from Spring Boot...')).not.toBeInTheDocument());

        // Click the Edit button for the first row
        const editButtons = screen.getAllByRole('button', { name: /Edit/i });
        fireEvent.click(editButtons[0]);

        // The edit row should now be visible. Change the student's major.
        const majorInput = screen.getByPlaceholderText('Student Major');
        fireEvent.change(majorInput, { target: { value: 'Software Engineering' } });

        // Click Save
        const saveButton = screen.getByRole('button', { name: /Save/i });
        fireEvent.click(saveButton);

        // Verify the update service was called with the modified data
        await waitFor(() => {
            expect(StudentService.updateStudent).toHaveBeenCalledWith(1, {
                name: 'John Doe',
                major: 'Software Engineering',
                gpa: 3.8
            });
        });
    });
});