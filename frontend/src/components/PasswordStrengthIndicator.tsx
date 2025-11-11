import React from 'react';

interface PasswordStrengthIndicatorProps {
  password: string;
  className?: string;
}

interface PasswordStrength {
  score: number;
  feedback: string;
}

const PasswordStrengthIndicator: React.FC<PasswordStrengthIndicatorProps> = ({ 
  password, 
  className = '' 
}) => {
  const calculatePasswordStrength = (password: string): PasswordStrength => {
    let score = 0;
    let feedback = '';

    if (password.length >= 8) score += 1;
    if (/[a-z]/.test(password)) score += 1;
    if (/[A-Z]/.test(password)) score += 1;
    if (/[0-9]/.test(password)) score += 1;
    if (/[^A-Za-z0-9]/.test(password)) score += 1;

    switch (score) {
      case 0:
      case 1:
        feedback = 'Very weak';
        break;
      case 2:
        feedback = 'Weak';
        break;
      case 3:
        feedback = 'Fair';
        break;
      case 4:
        feedback = 'Good';
        break;
      case 5:
        feedback = 'Strong';
        break;
      default:
        feedback = '';
    }

    return { score, feedback };
  };

  const getPasswordStrengthColor = (score: number): string => {
    switch (score) {
      case 0:
      case 1:
        return 'bg-red-500';
      case 2:
        return 'bg-orange-500';
      case 3:
        return 'bg-yellow-500';
      case 4:
        return 'bg-blue-500';
      case 5:
        return 'bg-green-500';
      default:
        return 'bg-gray-300';
    }
  };

  if (!password) {
    return null;
  }

  const strength = calculatePasswordStrength(password);

  return (
    <div className={`mt-2 ${className}`}>
      <div className="flex items-center space-x-2">
        <div className="flex-1 bg-gray-200 rounded-full h-2">
          <div 
            className={`h-2 rounded-full transition-all duration-300 ${getPasswordStrengthColor(strength.score)}`}
            style={{ width: `${(strength.score / 5) * 100}%` }}
          />
        </div>
        <span className="text-xs text-gray-600 min-w-[60px]">{strength.feedback}</span>
      </div>
    </div>
  );
};

export default PasswordStrengthIndicator;