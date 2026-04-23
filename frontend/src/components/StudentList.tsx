import React, {useEffect, useState} from 'react';
import { StudentService } from '../services/StudentService';
import {StudentRequest, StudentResponse} from '../types/student-reg-types';


const StudentList = () => {
    // State to store our students
    const [students, setStudents] = useState<StudentResponse[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);
    const [newStudentName, setNewStudentName] = useState("");
    const [newStudentMajor, setNewStudentMajor] = useState("");
    const [newStudentGPA, setNewStudentGPA] = useState("");
    const [editingStudentId, setEditingStudentId] = useState<number | null>(null);
    const [editForm, setEditorForm] = useState({
        name: "",
        major: "",
        gpa: ""

    });

    const loadStudents = async () => {
        try {
            const data = await StudentService.getAllStudents();
            setStudents(data);
        } catch (err) {
            setError(err instanceof Error ? err.message : 'An error occurred');
        } finally {
            setLoading(false);
        }
    };

    const handleAdd = async () => {
        // inputs always want to handle strings, so convert the max size to a number
        const numericGPA = parseFloat(newStudentGPA.trim());

        // try to save the course.
        const savedCourse = await StudentService.addStudent(
            { name: newStudentName,
                major: newStudentMajor,
                gpa: numericGPA,
            });
        console.log("I saved the students--now, to update the fields.")
        // refresh the list of students
        await loadStudents();
        //reset all the fields to clear the data

        setNewStudentName(""); // Clear the input
        setNewStudentMajor("");
        setNewStudentGPA("");

    };

    const handleEditClick = (student: any) => {
        setEditingStudentId(student.id);
        setEditorForm({
            name: student.name || "",
            major: student.major || "",
            gpa: student.gpa ? student.gpa.toString() : ""

        });
    };

    const handleSaveEdit = async (id:number) => {
        try {
            const updatedData = {
                name: editForm.name,
                major: editForm.major,
                gpa: parseFloat(editForm.gpa)

            };

            // Send PUT request to your Spring Boot controller
            await StudentService.updateStudent(id, updatedData);

            // Close the edit row and refresh the list
            setEditingStudentId(null);
            await loadStudents();
        } catch (err) {
            console.error("Failed to update course: ", err);
        }
    };

    const handleDelete = async (id: number) => {
        try {
            await StudentService.deleteStudent(id);
            // This triggers a re-render without a page refresh!
            setStudents(students.filter(s => s.id !== id));
        } catch (err) {
            alert("Delete failed!");
        }
    };



    // Fetch data on component load
    useEffect(() => {
        loadStudents();
    }, []);

    if (loading) return <div>Loading students from Spring Boot...</div>;
    if (error) return <div style={{ color: 'red' }}>Error: {error}</div>;


    return <div>
        <div id = "add-student-fields" className="student-container">
            <input id = "new-student-name"
                value={newStudentName}
                onChange={(e) => setNewStudentName(e.target.value)}
                placeholder="New Student Name"
            ></input>
            <input id = "new-student-major"
                value={newStudentMajor}
                onChange={(e) => setNewStudentMajor(e.target.value)}
                placeholder="New Student Major"
            ></input>
            <input id = "new-student-gpa"
                value={newStudentGPA}
                onChange={(e) => setNewStudentGPA(e.target.value)}
                placeholder = "New Student GPA"
            ></input>

            <button id = "add-student-button" onClick={handleAdd}>Add Student</button>
        </div>
    <h1>Students</h1>
    <table id = "student-list-table">
        <thead>
        <tr>
            <th>Student Name</th>
            <th>Major</th>
            <th>GPA</th>
            <th>Delete</th>
            <th>Edit</th>
        </tr>
        </thead>
        <tbody>
        {students.map(student => (
            <React.Fragment key={student.id}>
                <tr key={student.id} id = {`student-row-${student.id}`}>
                    <td id = {`student-name-${student.id}`} >{student.name}</td>
                    <td id = {`student-major-${student.id}`}>{student.major}</td>
                    <td id = {`student-gpa-${student.id}`}>{student.gpa}</td>

                    <td>
                        <button id = "delete-student-button"
                                onClick={() => handleDelete(student.id)} style={{color: 'red'}}>
                            Delete
                        </button>
                    </td>
                    <td>
                        <button id = "edit-student-button" onClick={() => handleEditClick(student)} style={{color: 'blue'}}>
                            Edit
                        </button>
                    </td>
                </tr>
                {/* Row 2: The Conditional Edit Row */}
                {editingStudentId === student.id && (
                    <tr style={{backgroundColor: '#f0f8ff'}}>
                        <td colSpan={7}> {/* Spans across all columns */}
                            <div style={{display: 'flex', gap: '10px', padding: '10px'}}>
                                <input id = "edit-student-name"
                                    value={editForm.name}
                                    onChange={(e) =>
                                        setEditorForm({...editForm, name: e.target.value})}
                                    placeholder="Student Name"
                                />
                                <input id = "edit-student-major"
                                    value={editForm.major}
                                    onChange={(e) =>
                                        setEditorForm({...editForm, major: e.target.value})}
                                    placeholder="Student Major"
                                />
                                <input id = "edit-student-gpa"
                                    value={editForm.gpa}
                                    onChange={(e) =>
                                        setEditorForm({...editForm, gpa: e.target.value})}
                                    placeholder="GPA"
                                />

                                <button id="edit-student-save-button"
                                    onClick={() => handleSaveEdit(student.id)} style={{color: 'green'}}>
                                    Save
                                </button>
                                <button id = "edit-student-cancel-button"
                                    onClick={() => setEditingStudentId(null)}>
                                    Cancel
                                </button>
                            </div>
                        </td>
                    </tr>
                )}
            </React.Fragment>
        ))}
        </tbody>
    </table>
    </div>
};

export default  StudentList;
