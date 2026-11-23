/* ==============================================================================
   CENTRALIZED API CLIENT & AUTHENTICATION MANAGER
   ============================================================================== */

const API_BASE = '/api';

const API = {
    // 0. AUTHENTICATION
    async login(username, password) {
        const res = await fetch(`${API_BASE}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        });
        const data = await res.json();
        if (!res.ok) throw new Error(data.message || 'Login failed');
        localStorage.setItem('currentUser', JSON.stringify(data));
        return data;
    },

    async signup(userData) {
        const res = await fetch(`${API_BASE}/auth/signup`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(userData)
        });
        const data = await res.json();
        if (!res.ok) throw new Error(data.message || 'Registration failed');
        localStorage.setItem('currentUser', JSON.stringify(data));
        return data;
    },

    // 1. STUDENTS
    async getStudents() {
        const res = await fetch(`${API_BASE}/students`);
        if (!res.ok) throw new Error('Failed to load students');
        return await res.json();
    },

    async addStudent(student) {
        const res = await fetch(`${API_BASE}/students`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(student)
        });
        const data = await res.json();
        if (!res.ok) throw new Error(data.message || 'Failed to add student');
        return data;
    },

    async deleteStudent(id) {
        const res = await fetch(`${API_BASE}/students/${id}`, {
            method: 'DELETE'
        });
        const data = await res.json();
        if (!res.ok) throw new Error(data.message || 'Failed to delete student');
        return data;
    },

    // 2. SUBJECTS
    async getSubjects() {
        const res = await fetch(`${API_BASE}/subjects`);
        if (!res.ok) throw new Error('Failed to load subjects');
        return await res.json();
    },

    // 3. MARKS
    async enterMark(markData) {
        const res = await fetch(`${API_BASE}/marks`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(markData)
        });
        const data = await res.json();
        if (!res.ok) throw new Error(data.message || 'Failed to save marks');
        return data;
    },

    async getMarksByStudent(rollNumber) {
        const res = await fetch(`${API_BASE}/marks/student/${encodeURIComponent(rollNumber)}`);
        if (!res.ok) throw new Error('Failed to load marks');
        return await res.json();
    },

    async getPerformanceForStudent(rollNumber) {
        const res = await fetch(`${API_BASE}/marks/performance/${encodeURIComponent(rollNumber)}`);
        if (!res.ok) throw new Error('Failed to load performance');
        return await res.json();
    },

    // 4. RANKINGS
    async getRankings() {
        const res = await fetch(`${API_BASE}/rankings`);
        if (!res.ok) throw new Error('Failed to load rankings');
        return await res.json();
    },

    // 5. ANALYTICS
    async getAnalytics() {
        const res = await fetch(`${API_BASE}/analytics`);
        if (!res.ok) throw new Error('Failed to load class analytics');
        return await res.json();
    },

    // 6. STUDY SCHEDULE OPTIMIZER
    async generateSchedule(rollNumber, availableDailyHours) {
        const res = await fetch(`${API_BASE}/schedule/generate`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ rollNumber, availableDailyHours: parseFloat(availableDailyHours) })
        });
        const data = await res.json();
        if (!res.ok) throw new Error(data.message || 'Failed to generate schedule');
        return data;
    }
};

// ==============================================================================
// AUTHENTICATION & SESSION GUARDS
// ==============================================================================

function getCurrentUser() {
    const raw = localStorage.getItem('currentUser');
    if (!raw) return null;
    try {
        return JSON.parse(raw);
    } catch {
        return null;
    }
}

function logout() {
    localStorage.removeItem('currentUser');
    window.location.href = '/login.html';
}

function checkAuth(requiredRole = null) {
    const user = getCurrentUser();
    if (!user) {
        window.location.href = '/login.html';
        return null;
    }

    if (requiredRole && user.role !== requiredRole) {
        // If student tries to open faculty page, redirect to student portal
        if (user.role === 'STUDENT') {
            window.location.href = '/student-dashboard.html';
        } else {
            window.location.href = '/index.html';
        }
        return null;
    }

    // Populate user profile info in sidebar
    const nameEl = document.getElementById('userDisplayName');
    const roleEl = document.getElementById('userDisplayRole');
    if (nameEl) nameEl.textContent = user.fullName;
    if (roleEl) roleEl.textContent = user.role === 'FACULTY' ? '👨‍🏫 Faculty Member' : `👨‍🎓 Roll: ${user.rollNumber || ''}`;

    return user;
}

function showAlert(alertElementId, message, isSuccess = true) {
    const el = document.getElementById(alertElementId);
    if (!el) return;
    el.textContent = message;
    el.className = isSuccess ? 'alert alert-success' : 'alert alert-error';
    el.style.display = 'block';

    setTimeout(() => {
        el.style.display = 'none';
    }, 4500);
}
