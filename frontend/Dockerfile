# /frontend/Dockerfile

# 1. Use an official Node.js image
FROM node:25

# 2. Set the working directory inside the container
WORKDIR /app

# 3. Copy only the package files first (for efficient caching)
COPY package*.json ./

# 4. Install dependencies
RUN npm install

# 5. Copy the rest of your React code
COPY . .

# Set the path environment variable for Vite, because container isn't finding it.
ENV PATH /app/node_modules/.bin:$PATH

# 6. Expose Vite's default port
EXPOSE 5173

# 7. Start Vite and expose it to the network so your browser can see it
CMD ["npm", "run", "dev", "--", "--host"]