import { useState } from 'react';
import { Link } from 'react-router-dom';
import { authService } from '../services/authService';
import ErrorNotification from '../components/ErrorNotification';

const LoginPage = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    try {
      await authService.login({ username, password });
      window.location.href = '/';
    } catch (err: any) {
      console.error('Login failed', err);
      setError(err.response?.data?.message || 'Login failed. Please check your credentials.');
    }
  };

  return (
    <div className="fade-in-up d-flex align-items-center justify-content-center" style={{ minHeight: '65vh' }}>
      <div style={{ width: '100%', maxWidth: '460px' }}>
        <div className="card glass-panel">
          <div className="card-body p-4 p-sm-5">
            <h2 className="text-center mb-1 fw-bold">Welcome Back</h2>
            <p className="text-center text-muted mb-4 small">Sign in to manage and participate in polls</p>
            <form onSubmit={handleSubmit}>
              <div className="mb-3">
                <label className="form-label">Username</label>
                <input 
                  type="text" 
                  className="form-control" 
                  placeholder="Enter your username"
                  value={username} 
                  onChange={(e) => setUsername(e.target.value)} 
                  required 
                />
              </div>
              <div className="mb-4">
                <label className="form-label">Password</label>
                <input 
                  type="password" 
                  className="form-control" 
                  placeholder="Enter your password"
                  value={password} 
                  onChange={(e) => setPassword(e.target.value)} 
                  required 
                />
              </div>
              <ErrorNotification message={error} />
              <button type="submit" className="btn btn-primary w-100 mt-2">Login</button>
            </form>
            <div className="mt-4 text-center">
              <p className="small text-muted mb-0">Don't have an account? <Link to="/register" className="fw-semibold">Register here</Link></p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
