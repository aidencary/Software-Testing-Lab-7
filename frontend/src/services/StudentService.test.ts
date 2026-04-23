import { describe, test, expect, beforeEach, vi, Mock } from 'vitest';
import { StudentService } from './StudentService';

// Hijack the browser's native fetch API
global.fetch = vi.fn();

describe('StudentService', () => {

    beforeEach(() => {
        // Reset the fake fetch history before every test
        vi.clearAllMocks();
    });

    // --- GET ALL STUDENTS ---
    describe('getAllStudents', () => {
        test('sends a GET request and returns a list of students', async () => {
            const mockStudents = [{ id: 1, name: 'John Doe', major: 'CS', gpa: 3.5 }];
            (global.fetch as Mock).mockResolvedValue({
                json: async () => mockStudents, // Mock the .json() promise
            });

            const result = await StudentService.getAllStudents();

            expect(result).toEqual(mockStudents);
            expect(fetch).toHaveBeenCalledWith('/api/students');
        });
    });

    // --- CREATE STUDENT (Returns JSON) ---
    describe('createStudent', () => {
        test('sends a POST request and returns a JSON message object', async () => {
            const mockResponse = { message: 'Student created successfully' };
            (global.fetch as Mock).mockResolvedValue({
                json: async () => mockResponse,
            });

            const newStudent = { name: 'Alice', major: 'Physics', gpa: 3.8 };
            const result = await StudentService.createStudent(newStudent);

            expect(result).toEqual(mockResponse);
            expect(fetch).toHaveBeenCalledWith(
                '/api/students',
                {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(newStudent),
                }
            );
        });
    });

    // --- ADD STUDENT (Returns Text) ---
    describe('addStudent', () => {
        test('sends a POST request and returns a string message', async () => {
            (global.fetch as Mock).mockResolvedValue({
                text: async () => 'Student added', // Mock the .text() promise
            });

            const newStudent = { name: 'Bob', major: 'Math' };
            const result = await StudentService.addStudent(newStudent);

            expect(result).toBe('Student added');
            expect(fetch).toHaveBeenCalledWith(
                '/api/students',
                {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(newStudent),
                }
            );
        });
    });

    // --- UPDATE STUDENT ---
    describe('updateStudent', () => {
        test('sends a PUT request with the updated student data', async () => {
            (global.fetch as Mock).mockResolvedValue({
                text: async () => 'Student updated',
            });

            const updateData = { gpa: 4.0 };
            const result = await StudentService.updateStudent(1, updateData);

            expect(result).toBe('Student updated');
            expect(fetch).toHaveBeenCalledWith(
                '/api/students/1',
                {
                    method: 'PUT',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(updateData),
                }
            );
        });
    });

    // --- DELETE STUDENT ---
    describe('deleteStudent', () => {
        test('sends a DELETE request for the specified ID', async () => {
            (global.fetch as Mock).mockResolvedValue({});

            await StudentService.deleteStudent(10);

            expect(fetch).toHaveBeenCalledWith(
                '/api/students/10',
                { method: 'DELETE' }
            );
        });
    });
});