import React from 'react';

interface ErrorNotificationProps {
  message: string;
}

const ErrorNotification: React.FC<ErrorNotificationProps> = ({ message }) => {
  if (!message) return null;
  
  return (
    <div className="alert alert-danger mt-3 d-flex align-items-center gap-2" role="alert">
      <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round" style={{ flexShrink: 0 }}>
        <circle cx="12" cy="12" r="10" />
        <line x1="12" y1="8" x2="12" y2="12" />
        <line x1="12" y1="16" x2="12.01" y2="16" />
      </svg>
      <div>{message}</div>
    </div>
  );
};

export default ErrorNotification;
