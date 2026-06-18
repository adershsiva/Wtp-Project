import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { authService } from '../services/authService';
import type { UserDTO } from '../types';
import ErrorNotification from '../components/ErrorNotification';

const RegisterPage = () => {
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    const newUser: UserDTO = { username, email, password };
    try {
      await authService.register(newUser);
      alert('Registration successful! Please login.');
      navigate('/login');
    } catch (err: any) {
      console.error('Registration failed', err);
      setError(err.response?.data?.message || 'Registration failed. Username or email might be taken.');
    }
  };

  return (
    <div className="fade-in-up d-flex align-items-center justify-content-center" style={{ minHeight: '70vh' }}>
      <div style={{ width: '100%', maxWidth: '460px' }}>
        <div className="card glass-panel">
          <div className="card-body p-4 p-sm-5">
            <h2 className="text-center mb-1 fw-bold">Create Account</h2>
            <p className="text-center text-muted mb-4 small">Join PollApp to start creating your own polls</p>
            <form onSubmit={handleSubmit}>
              <div className="mb-3">
                <label className="form-label">Username</label>
                <input 
                  type="text" 
                  className="form-control" 
                  placeholder="Choose a username"
                  value={username} 
                  onChange={(e) => setUsername(e.target.value)} 
                  required 
                />
              </div>
              <div className="mb-3">
                <label className="form-label">Email address</label>
                <input 
                  type="email" 
                  className="form-control" 
                  placeholder="your.email@example.com"
                  value={email} 
                  onChange={(e) => setEmail(e.target.value)} 
                  required 
                />
              </div>
              <div className="mb-4">
                <label className="form-label">Password</label>
                <input 
                  type="password" 
                  className="form-control" 
                  placeholder="Create a strong password"
                  value={password} 
                  onChange={(e) => setPassword(e.target.value)} 
                  required 
                />
              </div>
              <ErrorNotification message={error} />
              <button type="submit" className="btn btn-primary w-100 mt-2">Register</button>
            </form>
            <div className="mt-4 text-center">
              <p className="small text-muted mb-0">Already have an account? <Link to="/login" className="fw-semibold">Login here</Link></p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default RegisterPage;
