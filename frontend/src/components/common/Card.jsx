import React from 'react';
import './Card.css';

const Card = ({
    children,
    variant = 'default',
    hoverable = false,
    className = '',
    ...props
}) => {
    const cardClass = `card card-${variant} ${hoverable ? 'card-hoverable' : ''} ${className}`;

    return (
        <div className={cardClass} {...props}>
            {children}
        </div>
    );
};

export default Card;
