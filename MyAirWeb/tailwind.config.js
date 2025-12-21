/** @type {import('tailwindcss').Config} */
module.exports = {
    content: [
        './pages/**/*.{js,ts,jsx,tsx,mdx}',
        './components/**/*.{js,ts,jsx,tsx,mdx}',
        './app/**/*.{js,ts,jsx,tsx,mdx}',
    ],
    theme: {
        extend: {
            colors: {
                'united-blue': '#002244',
                'united-yellow': '#F9A825',
                'united-light-blue': '#64B5F6',
                'united-dark-blue': '#001020',
                'success-green': '#4CAF50',
                'error-red': '#F44336',
                'warning-orange': '#FF9800',
                'form-bg': '#F5F5F5',
                'card-bg': '#FFFFFF',
                'text-primary': '#212121',
                'text-secondary': '#757575',
                // Keep these for backward compatibility
                primary: '#002244',
                secondary: '#4CAF50',
            },
        },
    },
    plugins: [],
}

