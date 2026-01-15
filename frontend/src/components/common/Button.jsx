import React from 'react';
import './Button.css';

const Button = ({
    children,
    variant = 'primary',
    size = 'md',
    loading = false,
    disabled = false,
    icon = null,
    fullWidth = false,
    onClick,
    type = 'button',
    ...props
}) => {
    const className = `btn btn-${variant} btn-${size} ${fullWidth ? 'btn-full' : ''} ${loading || disabled ? 'btn-disabled' : ''}`;

    return (
        <button
            type={type}
            className={className}
            onClick={onClick}
            disabled={loading || disabled}
            {...props}
        >
            {loading ? (
                <span className="btn-spinner"></span>
            ) : (
                <>
                    {icon && <span className="btn-icon">{icon}</span>}
                    {children}
                </>
            )}
        </button>
    );
};

export default Button;
