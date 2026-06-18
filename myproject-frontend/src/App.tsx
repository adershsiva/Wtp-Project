import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import HomePage from './pages/HomePage';
import CreatePollPage from './pages/CreatePollPage';
import PollDetailPage from './pages/PollDetailPage';
import RegisterPage from './pages/RegisterPage';
import LoginPage from './pages/LoginPage';
import { authService } from './services/authService';
import { useState, useEffect } from 'react';
import './App.css';

function App() {
  const [user, setUser] = useState<{ id: number; username: string | null } | null>(null);
  const [isNavOpen, setIsNavOpen] = useState(false);

  useEffect(() => {
    setUser(authService.getCurrentUser());
  }, []);

  const handleLogout = () => {
    authService.logout();
    setUser(null);
    window.location.href = '/login';
  };

  return (
    <Router>
      <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
        <div className="container">
          <Link className="navbar-brand" to="/" onClick={() => setIsNavOpen(false)}>
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round" style={{ marginRight: '6px' }}>
              <path d="M12 20V10" />
              <path d="M18 20V4" />
              <path d="M6 20v-4" />
            </svg>
            PollApp
          </Link>
          <button 
            className="navbar-toggler border-0" 
            type="button" 
            onClick={() => setIsNavOpen(!isNavOpen)}
            aria-label="Toggle navigation"
            style={{ outline: 'none', boxShadow: 'none' }}
          >
            <span className="navbar-toggler-icon"></span>
          </button>
          <div className={`collapse navbar-collapse ${isNavOpen ? 'show' : ''}`}>
            <ul className="navbar-nav me-auto">
              <li className="nav-item">
                <Link className="nav-link" to="/" onClick={() => setIsNavOpen(false)}>Home</Link>
              </li>
              {user && (
                <li className="nav-item">
                  <Link className="nav-link" to="/create-poll" onClick={() => setIsNavOpen(false)}>Create Poll</Link>
                </li>
              )}
            </ul>
            <div className="d-flex align-items-center">
              {user ? (
                <>
                  <span className="text-light me-3">Welcome, {user.username}</span>
                  <button className="btn btn-outline-light btn-sm" onClick={() => { handleLogout(); setIsNavOpen(false); }}>Logout</button>
                </>
              ) : (
                <>
                  <Link className="btn btn-outline-light btn-sm me-2" to="/login" onClick={() => setIsNavOpen(false)}>Login</Link>
                  <Link className="btn btn-light btn-sm" to="/register" onClick={() => setIsNavOpen(false)}>Register</Link>
                </>
              )}
            </div>
          </div>
        </div>
      </nav>

      <main className="flex-grow-1 main-container">
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route path="/create-poll" element={<CreatePollPage />} />
          <Route path="/poll/:id" element={<PollDetailPage />} />
        </Routes>
      </main>

      <footer className="footer">
        <div className="container text-center">
          <p className="mb-0">&copy; 2026 Poll Management Application. Built with visual excellence.</p>
        </div>
      </footer>
    </Router>
  );
}

export default App;
