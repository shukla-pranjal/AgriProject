import React, { forwardRef } from 'react';
import './Input.css';

const Input = forwardRef(({
    label,
    error,
    helperText,
    icon,
    type = 'text',
    fullWidth = false,
    ...props
}, ref) => {
    return (
        <div className={`input-wrapper ${fullWidth ? 'input-full' : ''}`}>
            {label && <label className="input-label">{label}</label>}
            <div className="input-container">
                {icon && <span className="input-icon">{icon}</span>}
                <input
                    ref={ref}
                    type={type}
                    className={`input ${error ? 'input-error' : ''} ${icon ? 'input-with-icon' : ''}`}
                    {...props}
                />
            </div>
            {error && <span className="input-error-text">{error}</span>}
            {helperText && !error && <span className="input-helper-text">{helperText}</span>}
        </div>
    );
});

Input.displayName = 'Input';

export default Input;
