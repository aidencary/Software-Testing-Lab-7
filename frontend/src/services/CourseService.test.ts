import { describe, test, expect, beforeEach, vi, Mock } from 'vitest';
import { CourseService } from './CourseService';

// Hijack the browser's native fetch API
global.fetch = vi.fn();

describe('CourseService', () => {

    beforeEach(() => {
        // Reset the fake fetch history before every test
        vi.clearAllMocks();
    });

    // --- GET ALL COURSES ---
    describe('getAllCourses', () => {
        test('returns a list of courses on success', async () => {
            const mockCourses = [{ id: 1, name: 'Data Structures' }];
            (global.fetch as Mock).mockResolvedValue({
                ok: true,
                json: async () => mockCourses, // Mock the .json() promise
            });

            const result = await CourseService.getAllCourses();

            expect(result).toEqual(mockCourses);
            // We use expect.stringContaining to ignore whatever BASE_URL is set to
            expect(fetch).toHaveBeenCalledWith(expect.stringContaining(''));
        });

        test('throws an error when the response is not ok', async () => {
            (global.fetch as Mock).mockResolvedValue({
                ok: false,
            });

            // When testing if a function throws, we wrap it in a function and use reject/toThrow
            await expect(CourseService.getAllCourses()).rejects.toThrow('Failed to load courses');
        });
    });

    // --- ADD STUDENT TO COURSE ---
    describe('addStudentToCourse', () => {
        test('sends a PUT request and returns success message', async () => {
            (global.fetch as Mock).mockResolvedValue({
                ok: true,
                text: async () => 'Student added successfully',
            });

            const result = await CourseService.addStudentToCourse(100, 5);

            expect(result).toBe('Student added successfully');
            expect(fetch).toHaveBeenCalledWith(
                expect.stringContaining('/addStudent/100'),
                {
                    method: 'PUT',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(5),
                }
            );
        });

        test('throws an error with the server message if course is full', async () => {
            (global.fetch as Mock).mockResolvedValue({
                ok: false,
                text: async () => 'Course is full',
            });

            await expect(CourseService.addStudentToCourse(100, 5)).rejects.toThrow('Course is full');
        });
    });

    // --- REMOVE STUDENT FROM COURSE ---
    describe('removeStudentFromCourse', () => {
        test('sends a PUT request and returns success message', async () => {
            (global.fetch as Mock).mockResolvedValue({
                ok: true,
                text: async () => 'Student removed',
            });

            const result = await CourseService.removeStudentFromCourse(100, 5);

            expect(result).toBe('Student removed');
            expect(fetch).toHaveBeenCalledWith(
                expect.stringContaining('/removeStudent/100'),
                expect.objectContaining({ method: 'PUT', body: JSON.stringify(5) })
            );
        });
    });

    // --- GET ENROLLMENT COUNT ---
    describe('getEnrollmentCount', () => {
        test('returns the number of students', async () => {
            (global.fetch as Mock).mockResolvedValue({
                ok: true,
                json: async () => 24,
            });

            const result = await CourseService.getEnrollmentCount(100);
            expect(result).toBe(24);
        });

        test('returns -1 if the response is not ok', async () => {
            (global.fetch as Mock).mockResolvedValue({
                ok: false,
            });

            const result = await CourseService.getEnrollmentCount(999);
            expect(result).toBe(-1); // Verifying your custom fallback logic works
        });
    });

    // --- ADD COURSE ---
    describe('addCourse', () => {
        test('sends a POST request and returns a string message', async () => {
            (global.fetch as Mock).mockResolvedValue({
                ok: true,
                text: async () => 'Course created',
            });

            const newCourse = { name: 'Advanced Biology', maxSize: 30 };
            const result = await CourseService.addCourse(newCourse);

            expect(result).toBe('Course created');
            expect(fetch).toHaveBeenCalledWith(
                expect.any(String),
                {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(newCourse),
                }
            );
        });
    });

    // --- UPDATE COURSE ---
    describe('updateCourse', () => {
        test('sends a PUT request with the updated course data', async () => {
            (global.fetch as Mock).mockResolvedValue({
                ok: true,
                text: async () => 'Course updated',
            });

            const updateData = { room: 'Lab 2' };
            const result = await CourseService.updateCourse(100, updateData);

            expect(result).toBe('Course updated');
            expect(fetch).toHaveBeenCalledWith(
                expect.stringContaining('/100'),
                {
                    method: 'PUT',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(updateData),
                }
            );
        });
    });

    // --- DELETE COURSE ---
    describe('deleteCourse', () => {
        test('sends a DELETE request', async () => {
            (global.fetch as Mock).mockResolvedValue({
                ok: true,
            });

            await CourseService.deleteCourse(100);

            expect(fetch).toHaveBeenCalledWith(
                expect.stringContaining('/100'),
                { method: 'DELETE' }
            );
        });
    });
});